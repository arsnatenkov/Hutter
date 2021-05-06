ymaps.ready(init);

function init() {
    var customBalloonContentLayout = ymaps.templateLayoutFactory.createClass([
        '<ul class=list>',
        '{% for geoObject in properties.geoObjects %}',
        '<li><a href="/offer?id={{ geoObject.properties.offerId }}" ' +
        'data-placemarkid="{{ geoObject.properties.placemarkId }}" ' +
        'class="list_item">{{ geoObject.properties.balloonContentHeader|raw }}</a></li>',
        '{% endfor %}',
        '</ul>',
    ].join(''));

    var myMap = new ymaps.Map('map', {
            center: [55.754096, 37.649238],
            zoom: 16,
            behaviors: ['drag'],
            controls: ['zoomControl', 'searchControl'], //, 'fullscreenControl'
        }),
        counter = 0,
        clusterer = new ymaps.Clusterer({
            preset: 'islands#grayClusterIcons',
            groupByCoordinates: false,
            clusterDisableClickZoom: true,
            clusterHideIconOnBalloonOpen: false, // прячем иконку при открытии баллуна
            geoObjectHideIconOnBalloonOpen: false,
            clusterBalloonContentLayout: customBalloonContentLayout,
        }),
        addresses = [],
        ids = [],
        spaces = [],
        geoObjects = [];

    /**
     * Кластеризатор расширяет коллекцию, что позволяет использовать один обработчик
     * для обработки событий всех геообъектов.
     * Будем менять цвет иконок и кластеров при наведении.
     */
    clusterer.events
        // Можно слушать сразу несколько событий, указывая их имена в массиве.
        .add(['mouseenter', 'mouseleave'], function (e) {
            var target = e.get('target'),
                type = e.get('type');
            if (typeof target.getGeoObjects != 'undefined') {
                // Событие произошло на кластере.
                if (type === 'mouseenter') {
                    target.options.set('preset', 'islands#pinkClusterIcons');
                } else {
                    target.options.set('preset', 'islands#grayClusterIcons');
                }
            } else {
                // Событие произошло на геообъекте.
                if (type === 'mouseenter') {
                    target.options.set('preset', 'islands#pinkCircleDotIcon');
                } else {
                    target.options.set('preset', 'islands#grayCircleDotIcon');
                }
            }
        });

    for (var q = 0, l = document.getElementsByClassName("offer").length; q < l; ++q) {
        addresses[q] = document.getElementsByClassName("offer")[q].innerHTML;
        ids[q] = document.getElementsByClassName("offer")[q].getAttribute("id");
        spaces[q] = document.getElementsByClassName("space")[q].innerHTML;
    }

    for (var i = 0, len = addresses.length; i < len; ++i) {
        const id = i;
        ymaps.geocode(addresses[i]).then(function (res) {
            var coord = res.geoObjects.get(0).geometry.getCoordinates();
            const j = id;

            BalloonContentLayout = ymaps.templateLayoutFactory.createClass(
                '<div class="balloon-content">' +
                '<a href="/offer?id={{ properties.offerId }}">{{properties.name}}</a><br />' +
                // '<i id="count"></i> ' +
                // '<button id="counter-button"> [+1] </button>' +
                '</div>', {
                    build: function () {
                        BalloonContentLayout.superclass.build.call(this);
                        $('#counter-button').bind('click', this.onCounterClick);
                        $('#count').html(counter);
                    },
                    clear: function () {
                        $('#counter-button').unbind('click', this.onCounterClick);
                        BalloonContentLayout.superclass.clear.call(this);
                    },

                    onCounterClick: function () {
                        $('#count').html(++counter);
                        if (counter === 5) {
                            alert('Вы славно потрудились.');
                            counter = 0;
                            $('#count').html(counter);
                        }
                    }
                });

            MyBalloonLayout = ymaps.templateLayoutFactory.createClass(
                '<div class="popover top">' +
                '<a class="close" href="#">&times;</a>' +
                '<div class="arrow"></div>' +
                '<div class="popover-inner">' +
                '$[[options.contentLayout observeSize minWidth=235 maxWidth=235 maxHeight=350]]' +
                '</div>' +
                '</div>', {
                    build: function () {
                        this.constructor.superclass.build.call(this);
                        this._$element = $('.popover', this.getParentElement());
                        this.applyElementOffset();
                        this._$element.find('.close')
                            .on('click', $.proxy(this.onCloseClick, this));
                    },
                    clear: function () {
                        this._$element.find('.close')
                            .off('click');
                        this.constructor.superclass.clear.call(this);
                    },
                    onSublayoutSizeChange: function () {
                        MyBalloonLayout.superclass.onSublayoutSizeChange.apply(this, arguments);
                        if (!this._isElement(this._$element)) {
                            return;
                        }
                        this.applyElementOffset();
                        this.events.fire('shapechange');
                    },
                    applyElementOffset: function () {
                        // this._$element.css({
                        //     left: -(this._$element[0].offsetWidth / 2),
                        //     top: -(this._$element[0].offsetHeight + this._$element.find('.arrow')[0].offsetHeight)
                        // });
                    },
                    onCloseClick: function (e) {
                        e.preventDefault();
                        this.events.fire('userclose');
                    },
                    getShape: function () {
                        if (!this._isElement(this._$element)) {
                            return MyBalloonLayout.superclass.getShape.call(this);
                        }
                        var position = this._$element.position();
                        return new ymaps.shape.Rectangle(new ymaps.geometry.pixel.Rectangle([
                            [position.left, position.top], [
                                position.left + this._$element[0].offsetWidth,
                                position.top + this._$element[0].offsetHeight + this._$element.find('.arrow')[0].offsetHeight
                            ]
                        ]));
                    },
                    _isElement: function (element) {
                        return element && element[0] && element.find('.arrow')[0];
                    }
                });

            var placemark = new ymaps.Placemark(coord, {
                name: addresses[j],
                // balloonContent: addresses[j],
                // clusterCaption: spaces[j],
                balloonContentHeader: spaces[j],
                balloonContentBody: addresses[j],
                placemarkId: j,
                offerId: ids[j],
            }, {
                // iconLayout: 'default#image',
                // iconImageSize: [20, 20],
                // iconImageHref: "/images/search.svg",
                preset: 'islands#grayCircleDotIcon',
                balloonPanelMaxMapArea: 0,
                hideIconOnBalloonOpen: false,
                balloonOffset: [0, -10],
                // balloonLayout: MyBalloonLayout,
                balloonContentLayout: BalloonContentLayout,

            });

            // myMap.geoObjects.add(placemark);
            clusterer.add(placemark);
        });
    }

    clusterer.options.set({
        gridSize: 45,
        clusterDisableClickZoom: true
    });

    // clusterer.add(geoObjects);
    myMap.geoObjects.add(clusterer);

    myMap.setBounds(clusterer.getBounds(), {
        checkZoomRange: true
    });
}