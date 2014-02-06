/* 
 * Copyright (C) 2014 Chengwei.Yan.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

var rema = window.rema = window.rema || {version: "1.0"};
rema.init = function() {
    if (!Detector.webgl) {
        Detector.addGetWebGLMessage();
        return;
    }

    var container = document.getElementById('map');

    rema.map = new google.maps.Map(container, {
        zoom: 3,
        mapTypeControl: false,
        center: new google.maps.LatLng(10, 0),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        styles: rema.layer.styles
    });

    
    new rema.layer({map: rema.map}, function(layer) {

        var geometry = new THREE.Geometry(),
                texture = new THREE.Texture(rema.generateSprite()),
                material, particles;

        photos.forEach(function(photo) {
            var location = new google.maps.LatLng(photo[0], photo[1]),
                    vertex = layer.fromLatLngToVertex(location);

            geometry.vertices.push(vertex);
        });

        texture.needsUpdate = true;

        material = new THREE.ParticleBasicMaterial({
            size: 20,
            map: texture,
            opacity: 0.3,
            blending: THREE.AdditiveBlending,
            depthTest: false,
            transparent: true
        });

        particles = new THREE.ParticleSystem(geometry, material);
        layer.add(particles);

        gui = new dat.GUI();

        function update() {
            layer.render();
        }

        gui.add(material, 'size', 2, 100).onChange(update);
        gui.add(material, 'opacity', 0.1, 1).onChange(update);

    });
};
rema.layer = function(options, callback) {
    this.bindAll();
    this.callback = callback;
    this.initialize(options || {});

    this.firstRun = true;

    if (options.map) {
        this.setMap(options.map);
    }
};

rema.generateSprite = function() {


    var canvas = document.createElement('canvas'),
            context = canvas.getContext('2d'),
            gradient;

    canvas.width = 20;
    canvas.height = 20;

    gradient = context.createRadialGradient(
            canvas.width / 2, canvas.height / 2, 0,
            canvas.width / 2, canvas.height / 2, canvas.width / 2
            );

    gradient.addColorStop(1.0, 'rgba(255,255,255,0)');
    gradient.addColorStop(0.0, 'rgba(255,255,255,1)');

    context.fillStyle = gradient;
    context.fillRect(0, 0, canvas.width, canvas.height);

    return canvas;

};

rema.layer.prototype = new google.maps.OverlayView();
rema.layer.CSS_TRANSFORM = (function() {
    var div = document.createElement('div');
    var props = [
        'transform',
        'WebkitTransform',
        'MozTransform',
        'OTransform',
        'msTransform'
    ];

    for (var i = 0; i < props.length; i++) {
        var prop = props[i];
        if (div.style[prop] !== undefined) {
            return prop;
        }
    }

    return props[0];
})();

rema.layer.prototype.bindAll = function() {
    var instance = this;

    function bind(name) {
        var method = instance[name];
        if (typeof method !== "function") {
            return;
        }
        instance[name] = function() {
            return method.apply(instance, arguments);
        };
    }

    for (var all in instance) {
        bind(all);
    }
};

rema.layer.prototype.initialize = function(options) {

    this.options = options;

    this.camera = new THREE.OrthographicCamera(0, 255, 0, 255, -3000, 3000);
    this.camera.position.z = 1000;

    this.scene = new THREE.Scene();

    this.renderer = new THREE.WebGLRenderer({
        clearColor: 0x000000,
        clearAlpha: 0
    });

    this.canvas = this.renderer.domElement;
};

rema.layer.prototype.onAdd = function() {

    this.map = this.getMap();

    this.getPanes().overlayLayer.appendChild(this.canvas);

    this.changeHandler = google.maps.event.addListener(
            this.map,
            'bounds_changed',
            this.draw
            );

    this.draw();
};

rema.layer.prototype.onRemove = function() {

    if (!this.map) {
        return;
    }

    this.map = null;

    this.canvas.parentElement.removeChild(this.canvas);

    if (this.changeHandler) {
        google.maps.event.removeListener(this.changeHandler);
        this.changeHandler = null;
    }
};

rema.layer.prototype.draw = function() {

    if (!this.map) {
        return;
    }

    var bounds = this.map.getBounds();

    var topLeft = new google.maps.LatLng(
            bounds.getNorthEast().lat(),
            bounds.getSouthWest().lng()
            );

    var projection = this.getProjection();
    var point = projection.fromLatLngToDivPixel(topLeft);

    this.canvas.style[rema.layer.CSS_TRANSFORM] = 'translate(' +
            Math.round(point.x) + 'px,' +
            Math.round(point.y) + 'px)';

    if (this.firstRun) {
        this.firstRun = false;

        if (this.callback) {
            this.callback(this);
        }
    }

    this.update();
};

rema.layer.prototype.resize = function() {

    if (!this.map) {
        return;
    }

    var div = this.map.getDiv(),
            width = div.clientWidth,
            height = div.clientHeight;

    if (width === this.width && height === this.height) {
        return;
    }

    this.width = width;
    this.height = height;

    this.renderer.setSize(width, height);
    this.update();
};

rema.layer.prototype.update = function() {

    var projection = this.map.getProjection(),
            zoom, scale, offset, bounds, topLeft;

    if (!projection) {
        return;
    }

    bounds = this.map.getBounds();

    topLeft = new google.maps.LatLng(
            bounds.getNorthEast().lat(),
            bounds.getSouthWest().lng()
            );

    zoom = this.map.getZoom();
    scale = Math.pow(2, zoom);
    offset = projection.fromLatLngToPoint(topLeft);

    this.resize();

    this.camera.position.x = offset.x;
    this.camera.position.y = offset.y;

    this.camera.scale.x = this.width / 256 / scale;
    this.camera.scale.y = this.height / 256 / scale;

    this.render();
};

rema.layer.prototype.render = function() {
    cancelAnimationFrame(this.animationFrame);
    this.animationFrame = requestAnimationFrame(this.deferredRender);
};

rema.layer.prototype.deferredRender = function() {
    if (typeof this.options.render === false) {
        return;
    } else if (typeof this.options.render === "function") {
        this.options.render();
    } else {
        this.renderer.render(this.scene, this.camera);
    }
};

rema.layer.prototype.add = function(geometry) {
    this.scene.add(geometry);
};

rema.layer.prototype.fromLatLngToVertex = function(latLng) {
    var projection = this.map.getProjection(),
            point = projection.fromLatLngToPoint(latLng),
            vertex = new THREE.Vector3();

    vertex.x = point.x;
    vertex.y = point.y;
    vertex.z = 0;

    return vertex;
};

rema.layer.styles = [
    {
        "stylers": [
            {"invert_lightness": true},
            {"saturation": -100},
            {"visibility": "on"}
        ]
    }, {
        "elementType": "labels",
        "stylers": [
            {"visibility": "off"}
        ]
    }, {
        "featureType": "landscape",
        "stylers": [
            {"color": "#000000"}
        ]
    }, {
        "featureType": "road",
        "stylers": [
            {"visibility": "off"}
        ]
    }, {
        "featureType": "poi",
        "stylers": [
            {"visibility": "off"}
        ]
    }, {
        "featureType": "administrative",
        "stylers": [
            {"visibility": "off"}
        ]
    }, {
        "featureType": "administrative.country",
        "elementType": "geometry",
        "stylers": [
            {"visibility": "on"}
        ]
    }
];