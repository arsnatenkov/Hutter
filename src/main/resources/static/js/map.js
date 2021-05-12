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
            zoom: 15,
            behaviors: ['drag'],
            controls: ['zoomControl', 'searchControl'],
        }),
        clusterer = new ymaps.Clusterer({
            preset: 'islands#grayClusterIcons',
            groupByCoordinates: false,
            clusterDisableClickZoom: true,
            clusterHideIconOnBalloonOpen: false,
            geoObjectHideIconOnBalloonOpen: false,
            clusterBalloonContentLayout: customBalloonContentLayout,
        }),
        addresses = [],
        ids = [],
        spaces = [];

    clusterer.events
        .add(['mouseenter', 'mouseleave'], function (e) {
            var target = e.get('target'),
                type = e.get('type');
            if (typeof target.getGeoObjects != 'undefined') {
                if (type === 'mouseenter') {
                    target.options.set('preset', 'islands#blackClusterIcons');
                } else {
                    target.options.set('preset', 'islands#grayClusterIcons');
                }
            } else {
                if (type === 'mouseenter') {
                    target.options.set('preset', 'islands#blackCircleDotIcon');
                } else {
                    target.options.set('preset', 'islands#grayCircleDotIcon');
                }
            }
        });

    for (var q = 0, l = document.getElementsByClassName("offer").length; q < l; ++q) {
        addresses[q] = document.getElementsByClassName("offer")[q].innerHTML;
        ids[q] = document.getElementsByClassName("offer")[q].getAttribute("id");
        spaces[q] = document.getElementsByClassName("space")[q].getAttribute("id");
    }

    for (var i = 0, len = addresses.length; i < len; ++i) {
        const id = i;
        ymaps.geocode(addresses[i]).then(function (res) {
            var coord = res.geoObjects.get(0).geometry.getCoordinates();
            const j = id;

            BalloonContentLayout = ymaps.templateLayoutFactory.createClass(
                '<div class="balloon-content">' +
                '<div><a href="/offer?id={{properties.offerId}}">{{properties.name}}</a>, {{properties.balloonContentHeader}}</div>' +
                '</div>', {
                    build: function () {
                        BalloonContentLayout.superclass.build.call(this);
                    },
                    clear: function () {
                        BalloonContentLayout.superclass.clear.call(this);
                    }
                });

            var placemark = new ymaps.Placemark(coord, {
                name: addresses[j],
                balloonContentHeader: spaces[j],
                balloonContentBody: addresses[j],
                placemarkId: j,
                offerId: ids[j],
            }, {
                preset: 'islands#grayCircleDotIcon',
                balloonPanelMaxMapArea: 0,
                hideIconOnBalloonOpen: false,
                balloonContentLayout: BalloonContentLayout,
            });

            clusterer.add(placemark);
        });
    }

    clusterer.options.set({
        gridSize: 45,
        clusterDisableClickZoom: true
    });

    myMap.geoObjects.add(clusterer);

    myMap.setBounds(clusterer.getBounds(), {
        checkZoomRange: true
    });
}