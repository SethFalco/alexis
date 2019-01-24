create table alexis.guild
(
  guild_id               bigint(18)              not null,
  guild_xp               int default '0'         not null,
  guild_prefix           varchar(32) default '>' null,
  previous_roles         bit default b'0'        not null,
  translate_enabled      bit default b'0'        not null,
  translate_private      bit default b'0'        not null,
  join_role_user_enabled bit default b'0'        null,
  join_role_user_id      bigint                  null,
  join_role_bot_enabled  bit default b'0'        null,
  join_role_bot_id       bigint                  not null,
  level_global_sync      bit default b'0'        not null,
  level_enabled          bit default b'0'        null,
  level_channel_id       bit                     null,
  level_message          varchar(2000)           null,
  music_name_sync        bit default b'0'        not null,
  constraint guild_guild_id_uindex
    unique (guild_id),
  constraint guild_join_role_id_uindex
    unique (join_role_user_id)
);

alter table alexis.guild
  add primary key (guild_id);

create table alexis.emote
(
  emote_id bigint(18) not null,
  guild_id bigint(18) not null
    comment 'Useful for pruning data, if we''re in the guild, but the emote doesn''t exist, it''s been removed.',
  constraint emotes_emote_id_uindex
    unique (emote_id),
  constraint emote_guild_guild_id_fk
    foreign key (guild_id) references guild (guild_id)
);

alter table alexis.emote
  add primary key (emote_id);

create table alexis.emote_usage
(
  emote_id    bigint(18)                          not null,
  emote_count int(4)                              not null,
  usage_time  timestamp default CURRENT_TIMESTAMP not null,
  constraint emote_usage_emote_emote_id_fk
    foreign key (emote_id) references emote (emote_id)
);

create table alexis.message_channel
(
  channel_id       bigint(18)              not null
    primary key,
  guild_id         bigint(18)              null,
  channel_language varchar(2) default 'EN' null
    comment 'The language this channel is using Alexis in.',
  clever_state     varchar(8000)           null
    comment 'The cleverstate this channel currently has with the cleverbot API.',
  constraint message_channel_guild_guild_id_fk
    foreign key (guild_id) references guild (guild_id)
)
  comment 'Generic message channels, this could be guild text channels, or private channels.';

create table alexis.role
(
  role_id  bigint(18) not null,
  guild_id bigint(18) not null,
  constraint role_role_id_uindex
    unique (role_id),
  constraint role_guild_guild_id_fk
    foreign key (guild_id) references guild (guild_id)
);

alter table alexis.role
  add primary key (role_id);

create table alexis.skill
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

create table alexis.skill_channel
(
  skill_id   int    not null,
  channel_id bigint not null,
  constraint skill_channel_skill_id_channel_id_uindex
    unique (skill_id, channel_id),
  constraint skill_channel_message_channel_channel_id_fk
    foreign key (channel_id) references message_channel (channel_id),
  constraint skill_channel_skill_skill_id_fk
    foreign key (skill_id) references skill (skill_id)
)
  comment 'A mapping of what channels are allocated in to each skill in order to earn XP.';

create table alexis.skill_milestone
(
  skill_ms_id   int auto_increment
    primary key,
  skill_id      int          not null,
  skill_ms_name varchar(128) not null,
  skill_ms_xp   int          not null
    comment 'The amount of XP required in order to achieve this requirement.',
  constraint skill_rewards_skill_id_reward_name_reward_xp_req_uindex
    unique (skill_id, skill_ms_name, skill_ms_xp),
  constraint skill_rewards_skills_skill_id_fk
    foreign key (skill_id) references skill (skill_id)
)
  comment 'The rewards that can be configured for attaining a certain level in a guild''s skill.';

create table alexis.assignable_role
(
  role_id     bigint(18) not null,
  skill_ms_id int        not null,
  constraint assignable_role_role_id_skill_ms_id_uindex
    unique (role_id, skill_ms_id),
  constraint assignable_role_role_role_id_fk
    foreign key (role_id) references role (role_id),
  constraint assignable_role_skill_milestone_skill_ms_id_fk
    foreign key (skill_ms_id) references skill_milestone (skill_ms_id)
);

create table alexis.status
(
  status_type tinyint(1) default '0' not null,
  status_text varchar(64)            null,
  status_url  varchar(256)           null
    comment 'This should only be populated if it''s for a Twitch channel.',
  constraint status_status_text_uindex
    unique (status_text)
)
  comment 'Discord statuses the bot should be displaying during runtime.';

create table alexis.user
(
  user_id      bigint(18) auto_increment
    primary key,
  user_xp      int default '0'                     not null,
  last_message timestamp default CURRENT_TIMESTAMP not null
    on update CURRENT_TIMESTAMP
);

create table alexis.member
(
  member_id int auto_increment
    primary key,
  guild_id  bigint(18)      not null,
  user_id   bigint(18)      not null,
  member_xp int default '0' not null,
  constraint members_guild_id_user_id_uindex
    unique (guild_id, user_id),
  constraint member_guild_guild_id_fk
    foreign key (guild_id) references guild (guild_id),
  constraint members_users_user_id_fk
    foreign key (user_id) references user (user_id)
)
  comment 'This refers to Discord member entities; different from a user entity.';

create table alexis.member_skill
(
  member_id int             not null,
  skill_id  int             not null,
  skill_xp  int default '0' not null,
  constraint member_skills_members_member_id_fk
    foreign key (member_id) references member (member_id),
  constraint member_skills_skills_skill_id_fk
    foreign key (skill_id) references skill (skill_id)
);

