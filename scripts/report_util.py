import pandas as pd


def select(data, **kwargs):
    for k, v in kwargs.items():
        data = data[data[k] == v]
    return data


def set_columns(data, **kwargs):
    for k, v in kwargs.items():
        data[k] = v
    return data


def make_categorical(data, column):
    data[column] = pd.Categorical(data[column], categories=list(data[column].unique()))
    return data
