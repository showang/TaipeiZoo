package me.showang.taipeizoo.model

data class PageInfo(
    val offset: Int,
    val size: Int,
    val total: Int
) {
    val hasNextPage: Boolean by lazy { offset + size < total }
}