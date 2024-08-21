import sys
import os

import numpy as np
from arch.bootstrap import IndependentSamplesBootstrap

import performance
from report_util import *
from trial_data import extract


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


def style_table(table, title):
    values = pivot_performance_table(table)
    sigs = pivot_performance_table(create_sig_table(table))
    formats = {c: "{:,.0f}" for c in values.columns if 'Base' in c}
    formats.update({c: "{:,.2f}" for c in values.columns if 'Base' not in c})
    return values.style.format(formats, na_rep='---') \
        .apply(lambda _: sigs, axis=None) \
        .set_caption(title)


def process_performance_data(data):
    memory = create_performance_table(data, 'rss')
    s_memory = style_table(memory, 'Peak Memory Usage.')
    time = create_performance_table(data, 'elapsed_time')
    s_time = style_table(time, 'Execution Time')
    # TODO


def main():
    input_dir = sys.argv[1]
    output_file = sys.argv[2]
    functional_data, performance_data = extract(input_dir, input_dir)
    if performance_data is not None:
        process_performance_data(performance_data)


if __name__ == '__main__':
    main()
