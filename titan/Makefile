all: init

init:
	createdb titan
	createdb titan_test

migrate:
	lein run -m titan.db.migrations/migrate

rollback:
	lein run -m titan.db.migrations/rollback

rebuild:
	dropdb titan || :
	dropdb titan_test || :
	make init
	make migrate

