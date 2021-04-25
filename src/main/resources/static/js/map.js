ymaps.ready(init);

function init() {
    var myMap = new ymaps.Map('map', {
            center: [55.754096, 37.649238],
            zoom: 16,
            size: (400, 400),
            behaviors: ['drag', 'scrollZoom'],
            controls: ['zoomControl', 'searchControl'], //, 'fullscreenControl'
        }),
        clusterer = new ymaps.Clusterer({
            preset: 'islands#blackClusterIcons',
            groupByCoordinates: false,
            clusterDisableClickZoom: true,
            clusterHideIconOnBalloonOpen: false,
            geoObjectHideIconOnBalloonOpen: false
        }),

        getPointData = function (index) {
            return {
                balloonContent: '<div class="content"><a>address</a><br>content</div>',
                // balloonContent: '<div class="content"><a target="_blank" href="">address</a><br>content</div>',
                clusterCaption: 'space ' + index +''
            };
        },
        getPointOptions = function () {
            return {
                preset: 'islands#blackCircleDotIcon',
            };
        },
        addresses = [
            "улица Лётчика Бабушкина, 17", "улица Санникова, 17с2", "Покровский б-р, д.18, стр.18",
            "Покровский б-р, д.18, стр.18", "Россия, Москва, Ивовая улица, 2", "Россия, Москва, Игарский проезд, 8"
        ],
        geoObjects = [];

    for (var i = 0, len = addresses.length; i < len; ++i) {
        ymaps.geocode(addresses[i]).then(function (res) {
            var coord = res.geoObjects.get(0).geometry.getCoordinates();
            var myPlacemark = new ymaps.Placemark(coord, getPointData(i), getPointOptions());
            myMap.geoObjects.add(myPlacemark);
        });
    }

    clusterer.options.set({
        gridSize: 50,
        clusterDisableClickZoom: true
    });

    clusterer.add(geoObjects);

    myMap.geoObjects.add(clusterer);

    myMap.setBounds(clusterer.getBounds(), {
        checkZoomRange: true
    });
}