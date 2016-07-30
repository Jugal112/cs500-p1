-- CS 500 - Project 1: HealthyMe
-- Meghna Malhotra, Jugal Lodaya
-- 07.28.’16

-- Drop Existing Tables
drop table if exists Users cascade;
drop table if exists have_BodyStats;
drop table if exists walk_Steps;
drop table if exists perform_Activities;
drop table if exists need_Nutrition;
drop table if exists need_Sleep;
drop table if exists have_HeartRate;

-- Create New Tables
create table Users (
   user_id integer primary key,
   first_name varchar(128) not null,
   last_name varchar(128) not null,
   age integer not null,
   unique (first_name, last_name)
);

create table have_BodyStats (
   stat_id integer not null,
   user_id integer not null,
   height numeric not null, 
   weight numeric not null,
   primary key (stat_id, user_id),
   foreign key (user_id) references Users(user_id) on delete cascade
);

create table walk_Steps(
   step_id integer not null,
   user_id integer not null,
   date_x date not null,
   num_steps integer not null,
   calories_burned numeric not null,
   primary key (step_id, user_id),
   foreign key (user_id) references Users(user_id) on delete cascade
);

create table perform_Activities (
   activity_id integer not null,
   user_id integer not null,
   name varchar(128) not null,
   calories_burned numeric not null,
   date_x date not null,
   start_time time not null,
   end_time time not null,
   primary key (activity_id, user_id),
   foreign key (user_id) references Users(user_id) on delete cascade
);

create table need_Nutrition (
   meal_id integer not null,
   user_id integer not null,
   food_name varchar(128) not null,
   meal_type varchar(64) not null,
   calories numeric not null,
   date_x date not null,
   primary key (meal_id, user_id),
   foreign key (user_id) references Users(user_id) on delete cascade
);

create table need_Sleep (
   sleep_session_id integer not null,
   user_id integer not null,
   date_x date not null,
   start_time time not null,
   end_time time not null,
   primary key (sleep_session_id, user_id),
   foreign key (user_id) references Users(user_id) on delete cascade
);

create table have_HeartRate (
   hr_id integer not null,
   user_id integer not null,
   heart_rate integer not null,
   date_x date not null,
   start_time time not null,
   end_time time not null,
   primary key (hr_id, user_id),
   foreign key (user_id) references Users(user_id) on delete cascade
);
