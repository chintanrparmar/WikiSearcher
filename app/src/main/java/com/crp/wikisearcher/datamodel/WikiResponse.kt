package com.crp.wikisearcher.datamodel

data class WikiResponse(
    val batchcomplete: Boolean?,
    val `continue`: Continue?,
    val query: Query?
)