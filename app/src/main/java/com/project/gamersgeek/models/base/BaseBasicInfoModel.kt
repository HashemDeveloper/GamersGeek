package com.project.gamersgeek.models.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class BaseBasicInfoModel constructor(@SerializedName("id") @Expose open var id: Int?=null,
                                              @SerializedName("slug") @Expose open var slug: String?=null,
                                              @SerializedName("name") @Expose open var name: String?=null,
                                              @SerializedName("image") @Expose open var image: String?= null,
                                              @SerializedName("image_background") @Expose open var imageBackground: String?=null,
                                              @SerializedName("description") @Expose open var description: String?=null
)