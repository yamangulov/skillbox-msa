create schema users_scheme authorization msa;
create table users_scheme.Companies (
                                        ID uuid unique default gen_random_uuid(),
                                        name varchar not null ,
                                        phone varchar not null
);
create table users_scheme.Addresses (
                                        ID uuid unique default gen_random_uuid(),
                                        City varchar not null,
                                        Street varchar not null,
                                        House_number varchar not null,
                                        Flat_number integer
);
create table users_scheme.Companies_addresses (
                                                  ID uuid unique default gen_random_uuid(),
                                                  Companies_ID uuid not null,
                                                  Addresses_ID uuid not null,
                                                  foreign key (Companies_ID) references users_scheme.Companies(ID),
                                                  foreign key (Addresses_ID) references users_scheme.Addresses(ID)
);
