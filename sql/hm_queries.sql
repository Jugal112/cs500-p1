-- CS 500 - Project 1: HealthyMe
-- Meghna Malhotra, Jugal Lodaya
-- 07.30.’16

-- Database Queries

-- User Interaction Queries
-- Calculate current BMI (**Join)
select u.first_name, u.last_name, round(b.weight/(b.height*b.height), 3) as BMI 
from users u, have_bodystats b 
where u.user_id = b.user_id 
and u.first_name='Meghna' 
and u.last_name='Malhotra';

-- Calorie breakdown for date X for user Y (**Join)
select n.food_name, n.meal_type, n.calories 
from users u, need_nutrition n 
where u.user_id = n.user_id 
and u.first_name = 'Meghna' 
and u.last_name = 'Malhotra' 
and date_x = '2016-07-30';

-- Total calories consumed on date X by user Y (**Join and Group by)
select u.first_name, u.last_name, n.date_x, sum(n.calories) as total_calories_consumed 
from users u, need_nutrition n 
where u.user_id = n.user_id 
and u.first_name = 'Meghna' 
and u.last_name = 'Malhotra' 
and n.date_x = '2016-07-30' 
group by u.first_name, u.last_name, n.date_x; 

-- Total calories burned on date X by user Y (**Join and Group by)
select u.first_name, u.last_name, s.date_x, sum(a.calories_burned)+sum(s.calories_burned) as total_calories_burned 
from users u, perform_activities a, walk_steps s 
where u.user_id = a.activity_id
and u.user_id = s.step_id
and a.date_x = s.date_x 
and a.date_x = '2016-07-04' 
and u.first_name = 'Ford' 
and u.last_name = 'Prefect' 
group by u.first_name, u.last_name, s.date_x;

-- Calculate average steps walked per day, from date X to date Y (**Join and Group by)

-- Average calories burned per day, from date X to date Y (**Join and Group by)

-- Average calories consumed per day, from date X to date Y (**Join)

-- Average heart rate during activity from date X to date Y, per day (**Join and group by)

-- Average heart rate during sleep from date X to date Y, per day (**Join and group by)

-- Average resting heart rate for each day, from date X to date Y (**Join)
 
-- Insert Queries
-- Add user
select max(user_id) from users;
insert into Users(user_id, first_name, last_name, age) values (11, 'Meghna', 'Malhotra', 21);

-- Add body stats 
select max(stat_id) from have_bodystats;
insert into have_bodystats(stat_id, user_id, height, weight) values (11, 11, 1.58, 56.7);

-- Add meal 
select max(meal_id) from need_nutrition;
insert into need_nutrition(meal_id, user_id, food_name, meal_type, calories, date_x) values (20, 11, 'Yoghurt with Muesli and Fruits', 'breakfast', 250, '2016-07-30');
insert into need_nutrition(meal_id, user_id, food_name, meal_type, calories, date_x) values (21, 11, 'Aloo Paratha', 'lunch', 330, '2016-07-30');  
insert into need_nutrition(meal_id, user_id, food_name, meal_type, calories, date_x) values (22, 11, 'Pretzels with Nutella', 'dinner', 490, '2016-07-30');  
insert into need_nutrition(meal_id, user_id, food_name, meal_type, calories, date_x) values (23, 11, 'Chocolate Ice Cream', 'snack', 200, '2016-07-30');  

-- Add exercise
select max(activity_id) from perform_activities;
insert into perform_activities(activity_id, user_id, name, calories_burned, date_x, start_time, end_time) values (10, 11, 'Strength Training', 240, '2016-07-30', '07:00:00', '08:00:00');
