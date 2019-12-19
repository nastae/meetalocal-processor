insert into volunteer_entity (id, name, lastname, email, phone_number, age, gender)
values (1, 'Salomeja', 'Chomicenkiene', 'aurisgo1998@gmail.com', '+3706425713', 20, 'FEMALE');

insert into volunteer_entity (id, name, lastname, email, phone_number, age, gender)
values (2, 'Kestas', 'Kestovskis', 'aurisgo1998@gmail.com', '+3706425713', 23, 'MALE');

insert into meeting_form_entity (id, email, phone_number, name_and_lastname, from_country, meeting_date, people_count, preferences, gender, status, age, volunteer)
values (10, 'saauris@gmail.com', '+3706425713', 'Rasa', 'Rumunija', '2020-11-11 13:23:44', '2', 'None', 'FEMALE', 'NEW', 18, null);

insert into hobby_entity (id, hobby, meeting_form)
values (1, 'Art', 10);

insert into meeting_agreement_entity (id, meeting_form, volunteer, is_agreed)
values (1, 10, 2, false)