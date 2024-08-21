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


def overhead(baseline, treatment):
    med_b = np.median(baseline)
    med_t = np.median(treatment)
    return 100.0 * ((med_t - med_b) / med_b)


def bootstrap_ci(data, statistic):
    # Note: BCa fails for the baseline on pmd for memory
    ci = IndependentSamplesBootstrap(*data, seed=4034) \
        .conf_int(statistic, reps=1_000, method='bc', size=0.95, tail='two')
    return ci[0, 0], ci[1, 0]


def create_performance_row(data, y, tool, benchmark, sig_level):
    baseline = select(data, benchmark=benchmark, tool='none')[y]
    if tool == 'none':
        # Baseline (no tool used)
        value = np.median(baseline)
        lower, upper = bootstrap_ci((baseline,), np.median)
        return dict(benchmark=benchmark, tool=tool, value=value, LCL=lower, UCL=upper, p=np.nan, a12=np.nan, sig='')
    treatment = select(data, benchmark=benchmark, tool=tool)[y]
    if len(treatment) == 0:
        # No samples available for tool on benchmark
        return dict(benchmark=benchmark, tool=tool, value=np.nan, LCL=np.nan, UCL=np.nan, p=np.nan, a12=np.nan, sig='')
    value = overhead(baseline, treatment)
    lower, upper = bootstrap_ci((baseline, treatment), overhead)
    if tool == 'galette':
        # The alternate tool
        return dict(benchmark=benchmark, tool=tool, value=value, LCL=lower, UCL=upper, p=np.nan, a12=np.nan, sig='')
    alternative = select(data, benchmark=benchmark, tool='galette')[y]
    p = mann_whitney(treatment, alternative)
    effect_size = a12(treatment, alternative)
    sig = ''
    if p < sig_level:
        sig = 'color: red;' if value < overhead(baseline, alternative) else 'color: green;'
    return dict(benchmark=benchmark, tool=tool, value=value, LCL=lower, UCL=upper, p=p, a12=effect_size, sig=sig)


def create_performance_table(data, y):
    rows = [create_performance_row(data, y, t, b, sig_level=0.05 / 3) for b in performance.BENCHMARKS for t in
            performance.TOOLS]
    return pd.DataFrame(rows)


def pivot_performance_table(table):
    table = format_tool_names(table) \
        .pivot(index=['benchmark'], values=['value', 'LCL', 'UCL'], columns=['tool']) \
        .reorder_levels(axis=1, order=['tool', None]) \
        .sort_index(axis=1) \
        .sort_index(axis=0) \
        .reindex(['Base', 'Galette', 'MirrorTaint', 'Phosphor'], axis=1, level=0) \
        .reindex(['value', 'LCL', 'UCL'], axis=1, level=1)
    table.index.names = [None for _ in table.index.names]
    table.columns.names = [None for _ in table.columns.names]
    table.columns = pd.MultiIndex.from_tuples([(tool, fix_column_name(tool, x)) for tool, x in table.columns])
    return table


def fix_column_name(tool, x):
    if x == 'value':
        return 'MED' if tool == 'Base' else 'OV%'
    return x


def create_sig_table(table):
    sig = pd.DataFrame(table)
    sig['value'] = sig['sig']
    sig['LCL'] = sig['sig']
    sig['UCL'] = sig['sig']
    return sig


def style_performance_table(table, title):
    values = pivot_performance_table(table)
    sigs = pivot_performance_table(create_sig_table(table))
    formats = {c: "{:,.0f}" for c in values.columns if 'Base' in c}
    formats.update({c: "{:,.2f}" for c in values.columns if 'Base' not in c})
    return values.style.format(formats, na_rep='---') \
        .apply(lambda _: sigs, axis=None) \
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
    failures = format_tool_names(failures)
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


def create_time_content(data):
    table = create_performance_table(data, 'elapsed_time')
    return style_performance_table(table, 'Execution Time').to_html()


def create_memory_content(data):
    table = create_performance_table(data, 'rss')
    return style_performance_table(table, 'Peak Memory Usage.').to_html()


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
        content += create_section('Execution Time', create_time_content, data=performance_data)
        content += create_section('Peak Memory Usage', create_memory_content, data=performance_data)
    write_report(report_file, content)


def main():
    create_report(sys.argv[1], sys.argv[2])


if __name__ == '__main__':
    main()
