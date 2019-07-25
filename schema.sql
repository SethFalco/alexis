create table alexis.guild
(
    guild_id bigint not null comment 'The ID of the Discord guild this represents.',
    guild_xp int default 0 not null comment 'The XP earned in total in this guild.',
    guild_prefix varchar(32) default '>' null comment 'The prefix to prepend to commands, or null if no prefix will be used.
The default is stored against the guild statically so that it doesn''t change should the bot ever change the default prefix.',
    join_role_user_id bigint null,
    join_role_bot_id bigint null,
    xp_level_multiplier decimal(3,2) default 1.00 not null comment 'The multiplier to apply to the XP/Level formula to calculate the level.',
    constraint guild_guild_id_uindex
        unique (guild_id),
    constraint guild_join_role_id_uindex
        unique (join_role_user_id)
);

alter table alexis.guild
    add primary key (guild_id);

create table alexis.emote
(
    emote_id bigint not null
        primary key,
    guild_id bigint not null comment 'Useful for pruning data, if we''re in the guild, but the emote doesn''t exist, it''s been removed.',
    constraint emote_guild_guild_id_fk
        foreign key (guild_id) references alexis.guild (guild_id)
)
    comment 'Data about emotes globally and the guild it''s in. (We store the guild it''s in as well so we can know where to check to know if it''s been deleted.)';

create table alexis.emote_usage
(
    emote_usage_id int auto_increment
        primary key,
    emote_id bigint not null,
    guild_id bigint not null comment 'The guild this emote was used in. (Doesn''t have to be the guild that owns the emote.)',
    occurences int(4) not null,
    usage_timestamp timestamp default CURRENT_TIMESTAMP not null,
    constraint emote_usage_emote_emote_id_fk
        foreign key (emote_id) references alexis.emote (emote_id)
);

create table alexis.guild_feature
(
    feature_id bigint auto_increment,
    guild_id bigint not null,
    feature_name varchar(32) not null,
    enabled bit default b'0' not null,
    modified_by bigint not null comment 'The ID of the member that modified this last.',
    modified_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'The last time this setting was modified.',
    constraint features_feature_id_uindex
        unique (feature_id),
    constraint features_guild_id_feature_name_uindex
        unique (guild_id, feature_name)
)
    comment 'Features that can be enabled or disabled with no other configuration.';

alter table alexis.guild_feature
    add primary key (feature_id);

create table alexis.guild_message
(
    message_id int auto_increment
        primary key,
    guild_id bigint not null,
    message_type int not null,
    message_channel_id bigint null,
    message varchar(2000) null comment 'If enabled, but message is NULL, then send the default message.',
    constraint message_guild_id_message_type_uindex
        unique (guild_id, message_type),
    constraint message_guild_guild_id_fk
        foreign key (guild_id) references alexis.guild (guild_id)
)
    comment 'Configurable messages that can be enabled, disabled, set to channels or custom messages.';

create table alexis.log_type
(
    log_type_id int auto_increment comment 'The ID of this log type.',
    log_type_name varchar(32) not null comment 'A display friendly name of this type of log.',
    description varchar(1028) not null comment 'A description of this log type.',
    constraint guild_log_type_log_type_id_uindex
        unique (log_type_id),
    constraint guild_log_type_log_type_name_uindex
        unique (log_type_name)
)
    comment 'An reference table representing all log types for a guild.';

alter table alexis.log_type
    add primary key (log_type_id);

create table alexis.log_subscription
(
    subscription_id int auto_increment comment 'The ID of this subscription.',
    guild_id bigint not null comment 'The guild that this is setting the log for.',
    log_type_id int(2) not null comment 'The type of log this row is configuring.',
    subscribed bit default b'0' not null comment 'If this type of log is enabled.',
    constraint guild_log_subscription_guild_id_log_type_uindex
        unique (guild_id, log_type_id),
    constraint log_subscription_subscription_id_uindex
        unique (subscription_id),
    constraint guild_log_subscription_guild_guild_id_fk
        foreign key (guild_id) references alexis.guild (guild_id),
    constraint guild_log_subscription_guild_log_type_log_type_id_fk
        foreign key (log_type_id) references alexis.log_type (log_type_id)
);

alter table alexis.log_subscription
    add primary key (subscription_id);

create table alexis.message_channel
(
    channel_id bigint(18) not null
        primary key,
    guild_id bigint(18) null,
    channel_language varchar(2) default 'EN' null comment 'The language this channel is using Alexis in.',
    clever_state varchar(8000) null comment 'The cleverstate this channel currently has with the cleverbot API.',
    constraint message_channel_guild_guild_id_fk
        foreign key (guild_id) references alexis.guild (guild_id)
)
    comment 'Generic message channels, this could be guild text channels, or private channels.';

create table alexis.role
(
    role_id bigint(18) not null,
    guild_id bigint(18) not null,
    constraint role_role_id_uindex
        unique (role_id),
    constraint role_guild_guild_id_fk
        foreign key (guild_id) references alexis.guild (guild_id)
);

alter table alexis.role
    add primary key (role_id);

create table alexis.skill
(
    skill_id int auto_increment
        primary key,
    guild_id bigint(18) not null,
    skill_name varchar(64) null,
    skill_enabled bit default b'1' not null,
    skill_notify bit default b'1' not null,
    constraint skill_guild_guild_id_fk
        foreign key (guild_id) references alexis.guild (guild_id)
)
    comment 'Alexis skills, guilds can create these to have custom skills.';

create table alexis.skill_channel
(
    skill_id int not null,
    channel_id bigint not null,
    constraint skill_channel_skill_id_channel_id_uindex
        unique (skill_id, channel_id),
    constraint skill_channel_message_channel_channel_id_fk
        foreign key (channel_id) references alexis.message_channel (channel_id),
    constraint skill_channel_skill_skill_id_fk
        foreign key (skill_id) references alexis.skill (skill_id)
)
    comment 'A mapping of what channels are allocated in to each skill in order to earn XP.';

create table alexis.skill_milestone
(
    skill_ms_id int auto_increment
        primary key,
    skill_id int not null,
    skill_ms_name varchar(128) not null,
    skill_ms_xp int not null comment 'The amount of XP required in order to achieve this requirement.',
    constraint skill_rewards_skill_id_reward_name_reward_xp_req_uindex
        unique (skill_id, skill_ms_name, skill_ms_xp),
    constraint skill_rewards_skills_skill_id_fk
        foreign key (skill_id) references alexis.skill (skill_id)
)
    comment 'The rewards that can be configured for attaining a certain level in a guild''s skill.';

create table alexis.assignable_role
(
    role_id bigint(18) not null,
    skill_ms_id int not null,
    constraint assignable_role_role_id_skill_ms_id_uindex
        unique (role_id, skill_ms_id),
    constraint assignable_role_role_role_id_fk
        foreign key (role_id) references alexis.role (role_id),
    constraint assignable_role_skill_milestone_skill_ms_id_fk
        foreign key (skill_ms_id) references alexis.skill_milestone (skill_ms_id)
);

create table alexis.status
(
    status_type tinyint(1) default 0 not null,
    status_text varchar(64) null,
    status_url varchar(256) null comment 'This should only be populated if it''s for a Twitch channel.',
    constraint status_status_text_uindex
        unique (status_text)
)
    comment 'Discord statuses the bot should be displaying during runtime.';

create table alexis.user
(
    user_id bigint auto_increment
        primary key,
    user_xp int default 0 not null,
    last_message timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table alexis.member
(
    member_id int auto_increment
        primary key,
    guild_id bigint(18) not null,
    user_id bigint not null,
    member_xp int default 0 not null,
    constraint members_guild_id_user_id_uindex
        unique (guild_id, user_id),
    constraint member_guild_guild_id_fk
        foreign key (guild_id) references alexis.guild (guild_id),
    constraint members_users_user_id_fk
        foreign key (user_id) references alexis.user (user_id)
)
    comment 'This refers to Discord member entities; different from a user entity.';

create table alexis.member_skill
(
    member_id int not null,
    skill_id int not null,
    skill_xp int default 0 not null,
    constraint member_skills_members_member_id_fk
        foreign key (member_id) references alexis.member (member_id),
    constraint member_skills_skills_skill_id_fk
        foreign key (skill_id) references alexis.skill (skill_id)
);

