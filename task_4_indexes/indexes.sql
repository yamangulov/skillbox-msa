create index if not exists sex_name_idx on users_scheme.sex using hash (name);

create index if not exists city_name_idx on users_scheme.sex using hash (name);

create index if not exists sex_id_city_id_idx on users_scheme.profile using hash (sex_id, city_id);