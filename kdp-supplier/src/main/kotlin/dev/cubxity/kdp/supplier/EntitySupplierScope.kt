/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020-2021 Cubxity.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.kdp.supplier

import dev.cubxity.kdp.behavior.GuildBehavior
import dev.cubxity.kdp.behavior.MemberBehavior
import dev.cubxity.kdp.behavior.MessageBehavior
import dev.cubxity.kdp.behavior.channel.ChannelBehavior
import dev.cubxity.kdp.behavior.channel.GuildChannelBehavior
import dev.cubxity.kdp.behavior.channel.GuildMessageChannelBehavior
import dev.cubxity.kdp.behavior.channel.MessageChannelBehavior
import dev.cubxity.kdp.behavior.holder.GuildBehaviorHolder
import dev.cubxity.kdp.behavior.holder.MessageBehaviorHolder
import dev.cubxity.kdp.behavior.holder.channel.ChannelBehaviorHolder
import dev.cubxity.kdp.behavior.holder.channel.GuildChannelBehaviorHolder
import dev.cubxity.kdp.behavior.holder.channel.GuildMessageChannelBehaviorHolder
import dev.cubxity.kdp.behavior.holder.channel.MessageChannelBehaviorHolder
import dev.cubxity.kdp.entity.*
import dev.cubxity.kdp.entity.channel.Channel
import dev.cubxity.kdp.entity.channel.GuildChannel
import dev.cubxity.kdp.entity.channel.GuildMessageChannel
import dev.cubxity.kdp.entity.channel.MessageChannel
import kotlinx.coroutines.flow.Flow

/**
 * A scope associated with an entity supplier.
 */
interface EntitySupplierScope {
    /**
     * The [EntitySupplier] associated with this scope.
     */
    val supplier: EntitySupplier

    val GuildBehavior.members: Flow<Member>
        get() = supplier.getGuildMembers(id)

    val GuildBehavior.channels: Flow<GuildChannel>
        get() = supplier.getGuildChannels(id)

    val GuildBehavior.roles: Flow<Role>
        get() = supplier.getGuildRoles(id)

    val GuildBehavior.emojis: Flow<Emoji>
        get() = supplier.getEmojis(id)

    val MessageChannelBehavior.messages: Flow<Message>
        get() = supplier.getMessages(id, Position.After(Snowflake(0)))

    val MessageChannelBehavior.pinnedMessages: Flow<Message>
        get() = supplier.getChannelPins(id)

    suspend fun GuildBehavior.asGuildOrNull(): Guild? =
        this as? Guild ?: supplier.getGuildOrNull(id)

    suspend fun GuildBehavior.asGuild(): Guild =
        this as? Guild ?: supplier.getGuild(id)

    suspend fun GuildBehavior.getMemberOrNull(userId: Snowflake): Member? =
        supplier.getMemberOrNull(id, userId)

    suspend fun GuildBehavior.getMember(userId: Snowflake): Member =
        supplier.getMember(id, userId)

    suspend fun GuildBehavior.getRoleOrNull(roleId: Snowflake): Role? =
        supplier.getRoleOrNull(id, roleId)

    suspend fun GuildBehavior.getRole(roleId: Snowflake): Role =
        supplier.getRole(id, roleId)

    suspend fun GuildBehaviorHolder.getGuildOrNull(): Guild? =
        guild as? Guild ?: supplier.getGuildOrNull(guild.id)

    suspend fun GuildBehaviorHolder.getGuild(): Guild =
        guild as? Guild ?: supplier.getGuild(guild.id)

    suspend fun ChannelBehavior.asChannelOrNull(): Channel? =
        this as? Channel ?: supplier.getChannelOrNull(id)

    suspend fun ChannelBehavior.asChannel(): Channel =
        this as? Channel ?: supplier.getChannel(id)

    suspend fun ChannelBehaviorHolder.getChannelOrNull(): Channel? =
        channel as? Channel ?: supplier.getChannelOrNull(channel.id)

    suspend fun ChannelBehaviorHolder.getChannel(): Channel =
        channel as? Channel ?: supplier.getChannel(channel.id)

    suspend fun MessageChannelBehavior.asChannelOrNull(): MessageChannel? =
        this as? MessageChannel ?: supplier.getChannelOrNull(id) as? MessageChannel

    suspend fun MessageChannelBehavior.asChannel(): MessageChannel =
        this as? MessageChannel ?: supplier.getChannel(id) as MessageChannel

    suspend fun MessageChannelBehavior.getMessageOrNull(messageId: Snowflake): Message? =
        supplier.getMessageOrNull(id, messageId)

    suspend fun MessageChannelBehavior.getMessage(messageId: Snowflake): Message =
        supplier.getMessage(id, messageId)

    suspend fun MessageChannelBehaviorHolder.getChannelOrNull(): MessageChannel? =
        channel as? MessageChannel ?: supplier.getChannelOrNull(channel.id) as? MessageChannel

    suspend fun MessageChannelBehaviorHolder.getChannel(): MessageChannel =
        channel as? MessageChannel ?: supplier.getChannel(channel.id) as MessageChannel

    suspend fun GuildChannelBehavior.asChannelOrNull(): GuildChannel? =
        this as? GuildChannel ?: supplier.getChannelOrNull(id) as? GuildChannel

    suspend fun GuildChannelBehavior.getChannel(): GuildChannel =
        this as? GuildChannel ?: supplier.getChannel(id) as GuildChannel

    suspend fun GuildChannelBehavior.getGuildOrNull(): Guild? =
        this as? Guild ?: supplier.getGuildOrNull(guild.id)

    suspend fun GuildChannelBehavior.getGuild(): Guild =
        this as? Guild ?: supplier.getGuild(guild.id)

    suspend fun GuildChannelBehaviorHolder.getChannelOrNull(): GuildChannel? =
        channel as? GuildChannel ?: supplier.getChannelOrNull(channel.id) as? GuildChannel

    suspend fun GuildChannelBehaviorHolder.getChannel(): GuildChannel =
        channel as? GuildChannel ?: supplier.getChannel(channel.id) as GuildChannel

    suspend fun GuildMessageChannelBehavior.asChannelOrNull(): GuildMessageChannelBehavior? =
        this as? GuildMessageChannelBehavior ?: supplier.getChannelOrNull(id) as? GuildMessageChannelBehavior

    suspend fun GuildMessageChannelBehavior.asChannel(): GuildMessageChannelBehavior =
        this as? GuildMessageChannelBehavior ?: supplier.getChannel(id) as GuildMessageChannelBehavior

    suspend fun GuildMessageChannelBehaviorHolder.getChannelOrNull(): GuildMessageChannel? =
        channel as? GuildMessageChannel ?: supplier.getChannelOrNull(channel.id) as? GuildMessageChannel

    suspend fun GuildMessageChannelBehaviorHolder.getChannel(): GuildMessageChannel =
        channel as? GuildMessageChannel ?: supplier.getChannel(channel.id) as GuildMessageChannel

    suspend fun MemberBehavior.asMemberOrNull(): Member? =
        this as? Member ?: supplier.getMemberOrNull(guild.id, user.id)

    suspend fun MemberBehavior.asMember(): Member =
        this as? Member ?: supplier.getMember(guild.id, user.id)

    suspend fun MessageBehavior.getChannelOrNull(): MessageChannel? =
        channel as? MessageChannel ?: supplier.getChannelOrNull(id) as? MessageChannel

    suspend fun MessageBehavior.getChannel(): MessageChannel =
        channel as? MessageChannel ?: supplier.getChannel(id) as MessageChannel

    suspend fun MessageBehavior.asMessageOrNull(): Message? =
        this as? Message ?: supplier.getMessageOrNull(channel.id, id)

    suspend fun MessageBehavior.asMessage(): Message =
        this as? Message ?: supplier.getMessage(channel.id, id)

    suspend fun MessageBehaviorHolder.getMessageOrNull(): Message? =
        message as? Message ?: supplier.getMessageOrNull(message.channel.id, message.id)

    suspend fun MessageBehaviorHolder.getMessage(): Message =
        message as? Message ?: supplier.getMessage(message.channel.id, message.id)
}

fun EntitySupplierScope(supplier: EntitySupplier): EntitySupplierScope = object : EntitySupplierScope {
    override val supplier: EntitySupplier get() = supplier
}

inline fun <T> withSupplier(supplier: EntitySupplier, block: EntitySupplierScope.() -> T): T =
    EntitySupplierScope(supplier).block()
