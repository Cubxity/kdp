/*
 * KDP is a modular and customizable Discord command processing library.
 * Copyright (C) 2020 Cubxity.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.feature

import dev.cubxity.libs.kdp.pipeline.Pipeline

/**
 * Defines an installable bot feature
 * @param TPipeline is the type of the pipeline this feature is compatible with
 * @param TConfiguration is the type for the configuration object for this Feature
 * @param TFeature is the type for the instance of the Feature object
 */
interface KDPFeature<in TPipeline : Pipeline<*>, out TConfiguration : Any, TFeature : Any> {
    /**
     * Unique id that identifies the feature
     */
    val key: String

    /**
     * Function to install the feature
     */
    fun install(pipeline: TPipeline, configure: TConfiguration.() -> Unit): TFeature
}

/**
 * Installs [feature] into current pipeline
 * @param feature the feature to install
 * @param opt callback to configure the feature
 */
fun <P : Pipeline<*>, B : Any, F : Any> P.install(
    feature: KDPFeature<P, B, F>,
    opt: B.() -> Unit = {}
): F {
    return with (features[feature.key]) {
        when(this) {
            null -> {
                val f = feature.install(this@install, opt)
                features[feature.key] = f
                return f
            }
            else -> this as F
        }
    }
}