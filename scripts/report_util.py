import pandas as pd
import scipy

A12_BOUNDS = [0.56, 0.64, 0.71]


def select(data, **kwargs):
    for k, v in kwargs.items():
        data = data[data[k] == v]
    return data


def set_columns(data, **kwargs):
    for k, v in kwargs.items():
        data[k] = v
    return data


def a12(values1, values2):
    """
    Returns Vargha-Delaney A12 statistic.
    Vargha, A., and Delaney, H. D. (2000).
    A Critique and Improvement of the "CL" Common Language Effect Size Statistics of McGraw and Wong.
    Journal of Educational and Behavioral Statistics, 25(2), 101–132.
    https://doi.org/10.2307/1165329
    """
    a = scipy.stats.mannwhitneyu(values1, values2)[0] / (len(values1) * len(values2))
    return 1 - a if a < 0.5 else a


def mann_whitney(values1, values2):
    return scipy.stats.mannwhitneyu(values1, values2, alternative='two-sided', use_continuity=True)[1]


def compute_bucket(value, bounds):
    for i, bound in enumerate(bounds):
        if value < bound:
            return i
    return len(bounds)


def compute_cartesian_index(data, columns):
    categories = [data[c].unique() for c in columns]
    # Create an index that is the cartesian product of all unique values in the specified columns
    return pd.MultiIndex.from_product(categories, names=columns)


def format_tool_names(data):
    result = pd.DataFrame(data)
    result['tool'] = result['tool'] \
        .apply(lambda x: x.replace('none', 'base')) \
        .apply(str.title) \
        .apply(lambda x: x.replace('-', ''))
    return result
