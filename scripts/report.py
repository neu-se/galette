import os
import pathlib
import sys

import numpy as np
from arch.bootstrap import IndependentSamplesBootstrap

import performance
from report_util import *
from trial_data import extract

TEMPLATE = """
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        * {
            font-family: Open Sans, sans-serif;
            color: black;
        }

        h2 {
            font-size: 20px;
            font-weight: 550;
            display: block;
        }
        
        h3 {
            font-size: 12px;
            display: block;
        }

        table * {
            font-size: 10px;
            font-weight: normal;
            text-align: right;
            padding: 5px;
        }

        table {
            border-bottom: black 1px solid;
            border-top: black 1px solid;
            border-collapse: collapse;
        }
    </style>
    <title>Report</title>
</head>
<body>
<div>
    $content
</div>
</body>
</html>
"""

TABLE_NAMES = {
    'none': 'Baseline',
    'elapsed_time': 'Execution Time',
    'rss': 'Peak Memory Usage',
    'mirror-taint': 'MirrorTaint'
}


def format_table_names(table, *columns):
    result = pd.DataFrame(table)
    for column in columns:
        result[column] = result[column] \
            .apply(lambda n: TABLE_NAMES[n] if n in TABLE_NAMES else n.title())
    return result


def fix_column_names(names):
    if 'value' in names:
        names = list(names)
        names[names.index('value')] = 'MED' if 'Baseline' in names else 'OV'
    return names


def overhead(baseline, treatment):
    med_b = np.median(baseline)
    med_t = np.median(treatment)
    return (med_t - med_b) / med_b


def bootstrap_ci(data, statistic):
    # Note: BCa fails for the baseline on pmd for memory
    ci = IndependentSamplesBootstrap(*data, seed=4034) \
        .conf_int(statistic, reps=1_000, method='bc', size=0.95, tail='two')
    return ci[0, 0], ci[1, 0]


def create_performance_row(data, y, tool, benchmark, sig_level):
    result = dict(benchmark=benchmark, tool=tool, sig='', metric=y)
    for x in ['value', 'LCL', 'UCL', 'p', 'a12']:
        result[x] = np.nan
    treatment = select(data, benchmark=benchmark, tool=tool)[y]
    baseline = select(data, benchmark=benchmark, tool='none')[y]
    alternative = select(data, benchmark=benchmark, tool='galette')[y]
    if len(treatment) == 0:
        # No samples available for tool on benchmark
        return result
    elif tool == 'none':
        # Baseline (no tool used)
        value = np.median(treatment)
        lower, upper = bootstrap_ci((treatment,), np.median)
    else:
        value = overhead(baseline, treatment)
        lower, upper = bootstrap_ci((baseline, treatment), overhead)
    result.update(dict(value=value, LCL=lower, UCL=upper))
    if tool not in ['galette', 'none']:
        result['p'] = p = mann_whitney(treatment, alternative)
        result['a12'] = a12(treatment, alternative)
        if p < sig_level:
            result['sig'] = 'color: red;' if value < overhead(baseline, alternative) else 'color: green;'
    return result


def create_performance_table(data):
    # Convert kilobytes to megabytes
    data['rss'] = data['rss'] / 1000.0
    benchmarks = sorted(list(data['benchmark'].unique()))
    tools = performance.TOOLS
    sig_level = 0.05 / 3
    rows = [create_performance_row(data, 'elapsed_time', t, b, sig_level) for b in benchmarks for t in tools] \
           + [create_performance_row(data, 'rss', t, b, sig_level) for b in benchmarks for t in tools]
    return pd.DataFrame(rows)


def create_significance_mask(table, columns):
    result = pd.DataFrame(table)
    for column in columns:
        result[column] = result['sig']
    return result


def remove_multi_index_names(table):
    table.index.names = [None for _ in table.index.names]
    table.columns.names = [None for _ in table.columns.names]


def compute_performance_formats(columns):
    return {c: "{:,.0f}" if 'Baseline' in c and 'Execution Time' in c else "{:,.2f}" for c in columns}


def compute_p_table_formats(columns):
    return {c: ("{:.3E}" if 'p' in c else "{:,.3f}") for c in columns}


def pivot_performance_table(table, values):
    table = format_table_names(table, 'tool', 'metric') \
        .pivot(index=['benchmark'], values=values, columns=['tool', 'metric']) \
        .reorder_levels(axis=1, order=['metric', 'tool', None]) \
        .sort_index(axis=1) \
        .sort_index(axis=0) \
        .reindex(['Execution Time', 'Peak Memory Usage'], axis=1, level=0) \
        .reindex(['Baseline', 'Galette', 'MirrorTaint', 'Phosphor'], axis=1, level=1) \
        .reindex(values, axis=1, level=2)
    # Drop confidence limits for the baseline
    table.drop(inplace=True, columns=[c for c in table.columns if 'Baseline' in c and not 'value' in c])
    # Fix the placeholder 'value' column label
    table.columns = pd.MultiIndex.from_tuples([fix_column_names(c) for c in table.columns])
    # Remove multi-index names
    remove_multi_index_names(table)
    return table


def style_performance_table(table, values, title, format_f):
    # Create a mask table of formatting for statistically significant entries
    mask = create_significance_mask(table, values)
    # Pivot the table
    table = pivot_performance_table(table, values)
    # Apply formatting
    return table.style.format(format_f(table.columns), na_rep='---') \
        .apply(lambda _: pivot_performance_table(mask, values), axis=None) \
        .set_caption(title)


def compute_executions_counts(data):
    by = ['group', 'tool', 'version']
    executed = data.groupby(by)['status'] \
        .size() \
        .rename('executed') \
        .reset_index() \
        .drop_duplicates() \
        .reset_index(drop=True)
    return complete_cartesian_index(executed, by)


def create_count_table(data):
    executed = compute_executions_counts(data)
    by = ['group', 'tool', 'version']
    # Count the number of entries in each result for each group for each tool on each JDK
    counts = data.groupby(by)['result'] \
        .value_counts() \
        .reset_index()
    # Fill in zeros for missing combinations
    # Pivot along the results to put the results in columns
    categories = [data[c].unique() for c in by] + [['tag', 'semantic', 'success']]
    counts = complete_cartesian_index(counts, by + ['result'], categories=categories) \
        .pivot(columns='result', index=by, values='count') \
        .fillna(0) \
        .astype('int64') \
        .reset_index()
    # Compute the total number of tests per group
    totals = executed.groupby(['group'])['executed'] \
        .max() \
        .rename('total') \
        .reset_index() \
        .drop_duplicates()
    # Add totals to the table
    counts = counts.merge(totals, on=['group'], how='left')
    executed = executed[['group', 'version', 'executed']].drop_duplicates()
    # Added executed and totals to table
    counts = counts.merge(executed, on=['group', 'version'], how='left')
    # Drop rows for groups where nothing was executed (because the minimum version was not satisfied)
    counts = pd.DataFrame(counts[counts['executed'] != 0])
    # Rename the semantic column
    counts['sem'] = counts['semantic']
    return counts


def style_counts(counts):
    failures = counts.melt(id_vars=['group', 'tool', 'version', 'total'], value_vars=['sem', 'tag'])
    failures = format_table_names(failures)
    failures['variable'] = failures['variable'].apply(str.title)
    table = failures.pivot(index=['group', 'total', 'version'], values=['value'], columns=['tool', 'variable']) \
        .reorder_levels(axis=1, order=['tool', 'variable', None]) \
        .sort_index(axis=1) \
        .sort_index(axis=0) \
        .droplevel(2, axis=1)
    table.index.names = [x.title() for x in table.index.names]
    table.columns.names = [None for _ in table.columns.names]
    return table.style.format(precision=0, na_rep='---') \
        .set_caption('Semantics Preservation and Propagation Accuracy.')


def create_styled_performance_tables(data):
    table = create_performance_table(data)
    t1 = style_performance_table(table, ['value', 'LCL', 'UCL'],
                                 'Execution Time and Peak Memory Usage.',
                                 compute_performance_formats)
    selected = table[table['tool'].isin(['phosphor', 'mirror-taint'])]
    t2 = style_performance_table(selected, ['p', 'a12'], 'Execution Time and Peak Memory Usage P-Values.',
                                 compute_p_table_formats)
    return t1, t2


def create_performance_content(data):
    t1, t2 = create_styled_performance_tables(data)
    return f'{t1.to_html()}<br><br>{t2.to_html()}'


def create_functional_content(data):
    return style_counts(create_count_table(data)).to_html()


def create_section(name, content_f, **kwargs):
    print(f'Creating "{name}" section.')
    content = content_f(**kwargs)
    print(f'\tCreated "{name}" section.')
    return f'<div><h2>{name}</h2>{content}</div>'


def write_report(report_file, content):
    print(f'Writing report to {report_file}.')
    os.makedirs(pathlib.Path(report_file).parent, exist_ok=True)
    report = TEMPLATE.replace('$content', content)
    with open(report_file, 'w') as f:
        f.write(report)
    print(f'\tSuccessfully wrote report.')


def create_report(input_dir, report_file):
    functional_data, performance_data = extract(input_dir, input_dir)
    content = ""
    if functional_data is not None and not functional_data.empty:
        content += create_section('Semantics Preservation and Propagation Accuracy',
                                  create_functional_content, data=functional_data)
    if performance_data is not None and not performance_data.empty:
        content += create_section('Execution Time and Peak Memory Usage', create_performance_content,
                                  data=performance_data)
    write_report(report_file, content)


def main():
    create_report(sys.argv[1], sys.argv[2])


if __name__ == '__main__':
    main()
