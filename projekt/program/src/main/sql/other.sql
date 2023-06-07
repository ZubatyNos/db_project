

--jack the ripper monthly
select DISTINCT on (extract(month from date_of_fight))
    winner_id,
    count(winner_id) as all_defeated,
    extract(month from date_of_fight) as month,
    count(c.gender) filter (WHERE c.gender = 'F') as female_defeated,
    count(c.gender) filter (WHERE c.gender = 'M') as male_defeated
from fights
         join characters c on c.id = loser_id
group by (winner_id, month)
order by month, all_defeated desc;




--exhaustion stats

drop sequence if exists percentil;
create temp sequence percentil;

create or replace function overall(dummy integer) returns integer language sql immutable as
$$
select count(*)::integer
from fights f
where current_date - make_interval(0,0,0,7 + extract(day from date_of_fight)::integer % 7) <= f.date_of_fight
  and f.date_of_fight  <= current_date - make_interval(0,0,0,extract(day from date_of_fight)::integer % 7)
$$;



with best_day as (
    select DISTINCT ON (account_id)
        account_id,
        extract(day from date_of_fight) as day,
        count(*)
    from fights f
             join characters c on c.id = f.winner_id
    where current_date - make_interval(0,0,0,7 + extract(day from date_of_fight)::integer % 7) <= f.date_of_fight
      and f.date_of_fight  <= current_date - make_interval(0,0,0,extract(day from date_of_fight)::integer % 7)
    group by (c.account_id, day)
    order by account_id, count(*) desc
),
 best_hour as (
     select DISTINCT ON (account_id)
         account_id,
         extract(hour from date_of_fight) as hour,
         count(*)
     from fights f
              join characters c on c.id = f.winner_id
     where current_date - make_interval(0,0,0,7 + extract(day from date_of_fight)::integer % 7) <= f.date_of_fight
       and f.date_of_fight  <= current_date - make_interval(0,0,0,extract(day from date_of_fight)::integer % 7)
     group by (c.account_id, hour)
     order by account_id, count(*) desc
)

select
    tmp.account_id,
    tmp.kills,
    tmp.best_hour,
    tmp.best_day,
    (n < overall(0)/10) as percentil90
from (
         select
             c.account_id,
             count(*) as kills,
             bd.day as best_day,
             bh.hour as best_hour,
             nextval('percentil') as n
         from fights f
                  join characters c on f.winner_id = c.id
                  join best_day bd on c.account_id = bd.account_id
                  join best_hour bh on c.account_id = bh.account_id
         where current_date - make_interval(0,0,0,7 + extract(day from date_of_fight)::integer % 7) <= f.date_of_fight
           and f.date_of_fight  <= current_date - make_interval(0,0,0,extract(day from date_of_fight)::integer % 7)
         group by (c.account_id, bd.day, bh.hour)
         order by count(*) desc ) as tmp
limit 500;



drop function overall(dummy integer);
drop sequence percentil;


--bountyhunters

with best_bountyHunter as (
    SELECT winner_id, count(*)
    FROM fights f
             join characters c on c.id = f.winner_id
    where current_date - interval '1 month' <= f.date_of_fight and f.date_of_fight  <= current_date
    group by winner_id
    order by count(*) DESC
    limit 1
)
SELECT c.*
FROM ((select
           winner_id,
           count(*)
       FROM fights f
                join characters c2 on f.winner_id = c2.id and f.loser_id = (SELECT winner_id from best_bountyHunter)
       where current_date - interval '1 month' <= f.date_of_fight and f.date_of_fight <= current_date
       group by winner_id
       order by count(*) DESC
       LIMIT 3)
      UNION ALL
      (select * from best_bountyHunter)) as tmp
         join characters c on c.id = winner_id
order by tmp.count DESC;