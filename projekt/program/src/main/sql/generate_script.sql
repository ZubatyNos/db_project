truncate table services, accounts, transaction_history, abilities, classes,
class_abilities, races, race_classes, characters,
fights, items, character_items restart identity cascade;

drop table if exists equipment cascade;
create table equipment(name varchar);

drop table if exists prefix_for_equipment cascade;
create table prefix_for_equipment(prefix varchar, modifiers integer);

------------------------
--      pomocne tabulky
------------------------
insert into prefix_for_equipment (prefix, modifiers)
values ('broken', 2), ('weak', 4), ('powerful', 6), ('holy', 8), ('ungoldy', 10), ('otherworldy', 15);

insert into equipment (name)
values ('axe'), ('sword'), ('mace'), ('dagger'), ('staff'),
       ('breastplate'), ('shoes'), ('helmet'), ('pants'), ('shield');
---------------------
-- nenahodne data
---------------------

insert into services (type, credits)
values ('character transfer', 1000), ('appearance change', 200);

insert into races (name)
values	('human'), ('dwarf'), ('night elf'), ('gnome'),
        ('orc'), ('undead'), ('troll'), ('goblin');

insert into classes (name, max_health_per_level, defense_per_level, power_per_level)
values ('warrior', 100, 50, 65),      ('hunter', 65, 25, 35),
       ('priest', 50, 25, 35),        ('mage', 50, 30, 45);

insert into race_classes
values (1, 1), (1, 2), (2, 3), (2, 4),
       (3, 5), (3, 6), (4, 7), (4, 8);

insert into abilities (name, power_factor, healing)
values ('Attack',  1.5, false),
       ('Crippling blow',  2, false),
       ('Blitz attack',  2, false),
       ('Arrow storm',  2, false),
       ('Sky',  2, false),
       ('Restoration', 1, true),
       ('fireball', 3, false),
       ('electric discharge',  2, false),
       ('Blind', 2, false),
       ('Self-destruction', 2, false),
       ('Apply bandage', 1, true);

insert into class_abilities
values (1, 1), (1, 2), (1, 3), (1, 4),
       (2, 1),
       (3, 1),
       (4, 2),
       (5, 2),
       (6, 3), (6, 4),
       (7, 4),
       (8, 4),
       (9, 3),
       (10, 3),
       (11, 1), (11, 2), (11, 3), (11, 4);

insert into items (name, max_health_modifier, defense_modifier, power_modifier)
select
    p.prefix || ' ' || eq.name,
    modifiers * 2,
    modifiers / 2,
    modifiers
from equipment eq cross join prefix_for_equipment p;


------------------------
--      funkcie
------------------------

create or replace function random_class(race integer) returns integer language sql as
$$
    select id
    from classes c
    join race_classes rc on rc.class_id = c.id and rc.race_id = race
    order by random() limit 1
$$;

create or replace function sum_stats_of_all_equipped(dummy integer)
    returns table (char_id integer, hp integer) language sql as
$func$
select
    c.id as char_id,
    sum(max_health_modifier)::integer as hp
from characters c
         left join character_items c_i on c.id = c_i.character_id and c_i.equipped = true
         left join items i on i.id = c_i.item_id
group by c.id
$func$;


create or replace function num_of_rows(name varchar)
    returns integer language plpgsql IMMUTABLE as $func$
DECLARE
    ret integer;
BEGIN
    EXECUTE 'SELECT count(*)::integer FROM ' || quote_ident(name) INTO ret;
    RETURN ret;
end
$func$;

create or replace function two_random_numbers(first_random integer, first_random_interval integer)
    returns integer language plpgsql IMMUTABLE as $func$
DECLARE
    ret integer := floor(random()*first_random_interval) + 1;
BEGIN
    while ret = first_random loop
            ret = floor(random()*first_random_interval) + 1;
        end loop;
    RETURN ret;
end
$func$;

-----------------------------------
--      generovanie nahodnych dat
-----------------------------------

insert into accounts (username, mail, password, credits)
select
    'username' || id,
    'user' || id || '@mail.sk',
    (floor(random() * 8999999) + 1000000)::varchar,
    floor(random()*10000) + 2000
from generate_series(1,1000000) as id;

----------------------------------------------------------------------------------------------------------------------------

alter table transaction_history
    drop constraint transaction_history_service_id_fkey,
    drop constraint transaction_history_account_id_fkey;

drop index if exists transaction_history_account_id cascade;

insert into transaction_history (credits, description, service_id, account_id)
select
    case service
        when 0 then (select credits from services where type = 'character transfer') + floor(random()*500)
        else (select credits from services where type = 'appearance change')
    end,
    case service
        when 0 then 'character transfer'
        else 'appearance change'
    end,
    service + 1,
    floor(random()*num_of_rows('accounts') + 1)::integer
from (select floor(random()*2) as service from generate_series(1,1000000) as id) as tmp;

alter table transaction_history
    add FOREIGN KEY (service_id) references services on delete cascade,
    add foreign key (account_id) references accounts on delete cascade;

create index transaction_history_account_id on transaction_history (account_id);

----------------------------------------------------------------------------------------------------------------------------

alter table characters
    drop constraint characters_account_id_fkey,
    drop constraint characters_class_id_fkey,
    drop constraint characters_race_id_fkey,
    alter column current_health drop not null;

drop index if exists characters_class_id cascade;
drop index if exists characters_race_id cascade;
drop index if exists characters_account_id cascade;

insert into characters (name, level, experience, current_health, gender, hair_type, body_type, class_id, race_id, account_id)
select
    'character' || id as name,
    level,
    (level-1) * 100 + ((level-1) / 10) * 500 + ((level-1) / 25) * 5000 as experience,
    null, --tmp.class_hp  * level, --current health
    CASE floor(random()*3)
        when 1 then 'M'
        else 'F'
    end,    --gender
    floor(random()*5), --hair type
    floor(random()*5),
    random_class(race_id),
    race_id,
    floor(random()*num_of_rows('accounts') + 1)::integer
from (
    select
        i as id,
        floor(random() * 49) + 1 as level,
        sub.race_id
    from (select
                 i,
                 floor(random()*num_of_rows('races') + 1):: integer as race_id
            from generate_series(1,1000001) as i) as sub
) as tmp;

alter table characters
    add FOREIGN KEY (account_id) references accounts on delete cascade,
    add foreign key (race_id) references races on delete cascade,
    add foreign key (class_id) references classes on delete cascade;

create index characters_class_id on characters (class_id);
create index characters_race_id on characters (race_id);
create index characters_account_id on characters (account_id);

----------------------------------------------------------------------------------------------------------------------------

alter table fights
    drop constraint fights_loser_id_fkey,
    drop constraint fights_winner_id_fkey;

drop index if exists fights_winner_id cascade;
drop index if exists fights_loser_id cascade;
drop index if exists fights_date_winner_id cascade;

insert into fights
select
    winner_id,
    two_random_numbers(winner_id, num_of_rows('characters')::integer) as loser_id,
    timestamp '2021-01-01 00:00:00' + random() * interval '365 days'
from (select
          floor(random()*num_of_rows('characters')+1)::integer as winner_id
      from generate_series(1,3000000) as id
) as tmp;

alter table fights
    add foreign key (winner_id) references characters on delete cascade,
    add foreign key (loser_id) references characters on delete cascade;


create index fights_winner_id on fights (winner_id);
create index fights_loser_id on fights (loser_id);
create index fights_date_winner_id on fights (date_of_fight, winner_id);

----------------------------------------------------------------------------------------------------------------------------

alter table character_items
    drop constraint character_items_character_id_fkey,
    drop constraint character_items_item_id_fkey;

drop index if exists character_items_character_id cascade;

insert into character_items
select
    floor(random()*num_of_rows('characters')+1)::integer,
    floor(random()*num_of_rows('items')+1)::integer,
    floor(random()*2)::integer::bool
from generate_series(1,1000000) as id;


alter table character_items
    add foreign key (character_id) references characters on delete cascade,
    add foreign key (item_id) references items on delete cascade;

create index character_items_character_id on character_items (character_id);

----------------------------------------------------------------------------------------------------------------------------

update characters c
set
    current_health = cl.max_health_per_level * level + coalesce(s.hp, 0)
from sum_stats_of_all_equipped(0) s
         cross join classes cl
where s.char_id = c.id and cl.id = c.class_id;

alter table characters alter column current_health set not null;

----------------------------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------------------------
--      zahodenie pomocnych funckie a tabuliek
----------------------------------------------------------------------------------------------------------------------------

drop table prefix_for_equipment;

drop function random_class(race integer);
drop function sum_stats_of_all_equipped(char_id integer);
drop function num_of_rows(name varchar);
drop function two_random_numbers(first_random integer, first_random_interval integer);