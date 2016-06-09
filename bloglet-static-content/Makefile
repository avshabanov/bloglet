TARGET  = target/release/blogletWebsite
STATIC  =	$(TARGET)/web/static
FTL     = $(TARGET)/web/freemarker
NBIN		= ./node_modules/.bin
PORT		= 8000

.PHONY: clean browserify uglify libs test watch compile server static


# Targets

all: compile

node_modules:
	npm install --save-dev

target:
	mkdir -p $(FTL)
	mkdir -p $(STATIC)
	mkdir -p $(STATIC)/js

clean:
	rm -rf target

$(STATIC)/libs/bootstrap:
	mkdir -p $(STATIC)/libs/bootstrap

libs: target node_modules $(STATIC)/libs/bootstrap
	cp -r ./node_modules/bootstrap/dist/* $(STATIC)/libs/bootstrap

watch: node_modules libs target
	$(NBIN)/watchify js/main.js -o $(STATIC)/js/bundle.js -v -d -t babelify


# $(NBIN)/browserify . -t [envify --NODE_ENV production] -t [babelify --stage 0] | $(NBIN)/uglifyjs -cm > $(STATIC)/js/bundle.js

browserify: node_modules target
	$(NBIN)/browserify js/main.js -t babelify -o $(STATIC)/js/bundle.js

uglify: $(STATIC)/js/bundle.js
	cat $(STATIC)/js/bundle.js | $(NBIN)/uglifyjs -cm > $(STATIC)/js/bundle.min.js

static:
	mkdir -p $(STATIC)/css
	cp -r css/* $(STATIC)/css
	cp -r root/* $(STATIC)/
	cp -r freemarker/* $(FTL)

compile: libs browserify static

test: node_modules target
	npm test

server: node_modules target
	$(NBIN)/http-server $(STATIC) -p $(PORT) --utc


