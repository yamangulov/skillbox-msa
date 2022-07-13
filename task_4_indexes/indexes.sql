create index if not exists gender_id_idx on users_scheme.profile using hash (gender_id);

create index if not exists city_id_idx on users_scheme.profile using hash (city_id);

create index if not exists gender_id_city_id_idx on users_scheme.profile (gender_id, city_id);