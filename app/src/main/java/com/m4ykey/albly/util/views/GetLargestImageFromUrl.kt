package com.m4ykey.albly.util.views

fun <T, I> getLargestImageFromUrl(
    item : T,
    getImageList : (T) -> List<I>,
    getHeight : (I) -> Int,
    getWidth : (I) -> Int,
    getUrl : (I) -> String
) : String? {
    return getImageList(item).maxByOrNull { getHeight(it) * getWidth(it) }?.let(getUrl)
}