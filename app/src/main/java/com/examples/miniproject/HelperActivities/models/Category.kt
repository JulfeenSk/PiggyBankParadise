package com.examples.miniproject.HelperActivities.models

class Category {
    var categoryName: String? = null
    var categoryImage: Int = 0
    var categoryColor: Int = 0

    constructor()

    constructor(categoryName: String, categoryImage: Int, categoryColor: Int) {
        this.categoryName = categoryName
        this.categoryImage = categoryImage
        this.categoryColor = categoryColor
    }
}
