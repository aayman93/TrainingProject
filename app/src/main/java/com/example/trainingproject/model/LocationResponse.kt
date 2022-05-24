package com.example.trainingproject.model

data class LocationResponse(
    val code_status: Int,
    val `data`: Data,
    val message: String,
    val status: Int
) {
    data class Data(
        val current_page: Int,
        val `data`: List<Location>,
        val first_page_url: String,
        val from: Int,
        val last_page: Int,
        val last_page_url: String,
        val links: List<Link>,
        val next_page_url: String?,
        val path: String,
        val per_page: Int,
        val prev_page_url: Any,
        val to: Int,
        val total: Int
    ) {
        data class Link(
            val active: Boolean,
            val label: String,
            val url: Any
        )
    }
}