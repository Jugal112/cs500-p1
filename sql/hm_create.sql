-- CS 500 - Project 1: HealthyMe WebApp
-- Meghna Malhotra, Jugal Lodaya
-- 07.26.2016

-- Drop Existing Tables
drop table if exists Users;
drop table if exists Results;
drop table if exists Have_r;
drop table if exists Activities;
drop table if exists Perform;
drop table if exists Sleep;
drop table if exists Need_s;
drop table if exists HeartRate;
drop table if exists Have_a;
drop table if exists Nutrition;
drop table if exists Need_n;

-- Create New Tables

-- Users: 
-- All fields need to be full b/c we need them to calculate accurate results
-- Height in ft. Weight in lbs. Gender - M or F (for simplicity)
create table Users (
   user_id integer primary key,
   name varchar(128) not null,
   age integer not null,
   height numeric not null, 
   weight numeric not null,
   gender varchar(8) not null 
);

-- Results:
-- All fields will initially be empty and will be filled as each user
-- calculates their results. 
-- We need this table so that in the future, once a field has been
-- populated we can use the exiting value when we calculate the average values.
create table Results (
   user_id integer primary key,
   bmi real,
   resting_heart_rate real,
   avg_daily_calorie_intake real,
   avg_daily_calorie_burn real,
   avg_daily_steps real
);

-- Have_r:
-- Relationship between Users and Results
create table Have_r (
   user_id integer,
   result_id integer,
   primary key (user_id, result_id),
   foreign key (user_id) references Users(user_id) on delete cascade,
   foreign key (result_id) references Results(user_id) on delete cascade
);

-- Activities:
-- Each user can have 1 or more activities, although it is not necessary.
-- Examples: walking, cardio_exercise, yoga, strength_training, et al. 
create table Activities (
   activity_id integer primary key,
   calories_burned integer not null,
   exercise_type varchar(128) not null,
   avg_heart_rate integer not null,
   date_x date not null,
   time_interval interval not null,
   unique(time_interval, date_x)
);

-- Perform:
-- Relationship between Users and Activities
create table Perform (
   user_id integer,
   activity_id integer,
   primary key (user_id, activity_id),
   foreign key (user_id) references Users(user_id) on delete cascade,
   foreign key (activity_id) references Activities(activity_id) on delete cascade
);

-- Sleep:
-- Each user has to have a period of sleep each day
create table Sleep (
   sleep_session_id integer primary key,
   time_interval interval not null,
   date_x date not null,
   avg_heart_rate integer not null,
   sleep_quality varchar(128),
   unique(time_interval, date_x)
);

-- Need_s:
-- Relationship between Users and Sleep
create table Need_s (
   user_id integer,
   sleep_session_id integer,
   primary key (user_id, sleep_session_id),
   foreign key (user_id) references Users(user_id) on delete cascade,
   foreign key (sleep_session_id) references Sleep(sleep_session_id) on delete cascade
);

-- HeartRate:
-- Sometimes the value of the "heart_rate" column can be null
-- This happens when the user is not wearing their fitness tracker,
-- or when the tracker cannot cope with the speed of movement during
-- exercise and stops working for a few minutes.
-- NB. We do not want the date_x or time_x columns to be null at any time
-- But making them "primary key" takes care of that
create table HeartRate (
   time_x time,
   date_x date,
   heart_rate integer,
   primary key (time_x, date_x)
);

-- Have_a:
-- Relationship between Users and HeartRate
-- NB. A foreign key MUST be a candidate key in the table it's coming from.
-- Ideally, it's the primary key.
create table Have_a (
   user_id integer,
   time_x time,
   date_x date,
   primary key (user_id, time_x, date_x)
   foreign key (user_id) references Users(user_id) on delete cascade,
   foreign key (time_x, date_x) references HeartRate(time_x, date_x) on delete cascade
);

-- Nurition:
-- Users may or may not choose to manually log their meals
create table Nutrition (
   meal_id integer primary key,
   calories numeric not null,
   food_name varchar(128) not null,
);

-- Need_n:
-- Relationship between Users and Nutrition
create table Need_n (
   user_id integer,
   meal_id integer,
   primary key (user_id, meal_id),
   foreign key (user_id) references Users(user_id) on delete cascade,
   foreign key (meal_id) references Nutrition(meal_id) on delete cascade
);
