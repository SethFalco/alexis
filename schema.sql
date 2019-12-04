create table activity
(
    activity_id   int auto_increment
        primary key,
    activity_type tinyint      not null,
    activity_text varchar(256) not null,
    activity_url  varchar(256) null,
    constraint activity_activity_text_uindex
        unique (activity_text),
    constraint activity_activity_url_uindex
        unique (activity_url)
)
    comment 'Activities for the bot to cycle through.';

create table guild
(
    guild_id          bigint                        not null comment 'The ID of the Discord guild this represents.',
    guild_xp          int           default 0       not null comment 'The XP earned in total in this guild.',
    guild_locale      varchar(32)   default 'en_US' null comment 'The default locale used in the guild, this can be overridden per channel.',
    guild_prefix      varchar(32)   default '$'     null comment 'The prefix to prepend to commands, or null if no prefix will be used.
The default is stored against the guild statically so that it doesn''t change should the bot ever change the default prefix.',
    react_translate   bit           default b'0'    null comment 'If using country reactions should make the application translate the message.',
    join_role_user_id bigint                        null,
    join_role_bot_id  bigint                        null,
    xp_multp          decimal(3, 2) default 1.00    not null comment 'The multiplier to apply to the XP/Level formula to calculate the level.',
    constraint guild_guild_id_uindex
        unique (guild_id),
    constraint guild_join_role_id_uindex
        unique (join_role_user_id)
);

alter table guild
    add primary key (guild_id);

create table emote
(
    emote_id bigint not null
        primary key,
    guild_id bigint not null comment 'Useful for pruning data, if we''re in the guild, but the emote doesn''t exist, it''s been removed.',
    constraint emote_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id)
)
    comment 'Data about emotes globally and the guild it''s in. (We store the guild it''s in as well so we can know where to check to know if it''s been deleted.)';

create table emote_usage
(
    emote_usage_id  int auto_increment
        primary key,
    emote_id        bigint                              not null,
    guild_id        bigint                              not null comment 'The guild this emote was used in. (Doesn''t have to be the guild that owns the emote.)',
    occurences      int(4)                              not null,
    usage_timestamp timestamp default CURRENT_TIMESTAMP not null,
    constraint emote_usage_emote_emote_id_fk
        foreign key (emote_id) references emote (emote_id)
);

create table guild_feature
(
    feature_id   int auto_increment comment 'Internal identifier used by database to uniquely identify entries.'
        primary key,
    guild_id     bigint                              not null comment 'The guild this feature is enabled or disabled in.',
    feature_enum int                                 not null comment 'The internal enum ID of the feature as defined inside of the application.',
    enabled      bit       default b'0'              not null comment 'If this feature is enabled, or disabled.',
    modified_by  bigint                              not null comment 'The ID of the member that modified this last.',
    modified_at  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'The last time this setting was modified.',
    constraint guild_feature_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id)
)
    comment 'Features that can be enabled or disabled. Some may have additional configuration. Seperating the configuration rather than uses NULL as disabled allows users to disable features while preserving settings so it''s easy to enable again.';

create table guild_message
(
    message_id         int auto_increment
        primary key,
    guild_id           bigint        not null,
    message_type       int           not null,
    message_channel_id bigint        null,
    message            varchar(2000) null comment 'If enabled, but message is NULL, then send the default message.',
    constraint message_guild_id_message_type_uindex
        unique (guild_id, message_type),
    constraint message_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id)
)
    comment 'Configurable messages that can be enabled, disabled, set to channels or custom messages.';

create table log_type
(
    log_type_id   int auto_increment comment 'The ID of this log type.',
    log_type_name varchar(32)   not null comment 'A display friendly name of this type of log.',
    description   varchar(1028) not null comment 'A description of this log type.',
    constraint guild_log_type_log_type_id_uindex
        unique (log_type_id),
    constraint guild_log_type_log_type_name_uindex
        unique (log_type_name)
)
    comment 'An reference table representing all log types for a guild.';

alter table log_type
    add primary key (log_type_id);

create table log_subscription
(
    subscription_id int auto_increment comment 'The ID of this subscription.',
    guild_id        bigint           not null comment 'The guild that this is setting the log for.',
    log_type_id     int(2)           not null comment 'The type of log this row is configuring.',
    subscribed      bit default b'0' not null comment 'If this type of log is enabled.',
    constraint guild_log_subscription_guild_id_log_type_uindex
        unique (guild_id, log_type_id),
    constraint log_subscription_subscription_id_uindex
        unique (subscription_id),
    constraint guild_log_subscription_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id),
    constraint guild_log_subscription_guild_log_type_log_type_id_fk
        foreign key (log_type_id) references log_type (log_type_id)
);

alter table log_subscription
    add primary key (subscription_id);

create table message_channel
(
    channel_id      bigint(18)              not null
        primary key,
    guild_id        bigint(18)              null,
    channel_locale  varchar(5) default 'en' null comment 'The language this channel is using Alexis in.',
    cleverbot_state varchar(8000)           null comment 'The cleverstate this channel currently has with the cleverbot API.',
    constraint message_channel_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id)
)
    comment 'Generic message channels, this could be guild text channels, or private channels.';

create table role
(
    role_id  bigint(18) not null,
    guild_id bigint(18) not null,
    constraint role_role_id_uindex
        unique (role_id),
    constraint role_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id)
);

alter table role
    add primary key (role_id);

create table skill
(
    skill_id      int auto_increment
        primary key,
    guild_id      bigint(18)       not null,
    skill_name    varchar(64)      null,
    skill_enabled bit default b'1' not null,
    skill_notify  bit default b'1' not null,
    constraint skill_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id)
)
    comment 'Alexis skills, guilds can create these to have custom skills.';

create table skill_channel_relation
(
    relation_id     int auto_increment
        primary key,
    skill_id        int    not null,
    text_channel_id bigint not null,
    constraint skill_channel_skill_id_channel_id_uindex
        unique (skill_id, text_channel_id),
    constraint skill_channel_message_channel_channel_id_fk
        foreign key (text_channel_id) references message_channel (channel_id),
    constraint skill_channel_skill_skill_id_fk
        foreign key (skill_id) references skill (skill_id)
)
    comment 'A mapping of what channels are allocated to each skill in order to earn XP.';

create table skill_milestone
(
    skill_ms_id   int auto_increment
        primary key,
    skill_id      int          not null,
    skill_ms_name varchar(128) not null,
    skill_ms_xp   int          not null comment 'The amount of XP required in order to achieve this requirement.',
    constraint skill_rewards_skill_id_reward_name_reward_xp_req_uindex
        unique (skill_id, skill_ms_name, skill_ms_xp),
    constraint skill_rewards_skills_skill_id_fk
        foreign key (skill_id) references skill (skill_id)
)
    comment 'The rewards that can be configured for attaining a certain level in a guild''s skill.';

create table assignable_role
(
    role_id     bigint(18) not null,
    skill_ms_id int        null,
    constraint assignable_role_role_id_skill_ms_id_uindex
        unique (role_id, skill_ms_id),
    constraint assignable_role_role_role_id_fk
        foreign key (role_id) references role (role_id),
    constraint assignable_role_skill_milestone_skill_ms_id_fk
        foreign key (skill_ms_id) references skill_milestone (skill_ms_id)
);

create table user
(
    user_id       bigint auto_increment comment 'The Discord ID of the user.'
        primary key,
    user_xp       int       default 0                 not null comment 'The global XP this user has across all guilds where XP can be earned.',
    last_activity timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'When the user last interacted with the bot.'
)
    comment 'Represents a global Discord user.';

create table member
(
    member_id int auto_increment
        primary key,
    guild_id  bigint(18)    not null,
    user_id   bigint        not null,
    member_xp int default 0 not null,
    constraint members_guild_id_user_id_uindex
        unique (guild_id, user_id),
    constraint member_guild_guild_id_fk
        foreign key (guild_id) references guild (guild_id),
    constraint members_users_user_id_fk
        foreign key (user_id) references user (user_id)
)
    comment 'This refers to Discord member entities; different from a user entity.';

create table member_skill
(
    member_id int           not null,
    skill_id  int           not null,
    skill_xp  int default 0 not null,
    constraint member_skills_members_member_id_fk
        foreign key (member_id) references member (member_id),
    constraint member_skills_skills_skill_id_fk
        foreign key (skill_id) references skill (skill_id)
);

