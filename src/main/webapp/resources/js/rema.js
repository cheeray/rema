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

var rema = window.rema = window.rema || {version: "1.0", properties: []};
rema.init = function() {
    if (!Detector.webgl) {
        Detector.addGetWebGLMessage();
        return;
    }

    rema.container = document.getElementById('map');

    rema.map = new google.maps.Map(rema.container, {
        zoom: 4,
        mapTypeControl: false,
        center: new google.maps.LatLng(-33.8678500, 151.2073200),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        styles: rema.layer.styles
    });

    rema.draw = function() {

        rema.geometry = new THREE.Geometry();
        rema.texture = new THREE.Texture(rema.generateSprite());


        $.each(rema.properties, function(i, p) {
            var location = new google.maps.LatLng(p.geo.latitude, p.geo.longtitude);
            var vertex = rema.layer.fromLatLngToVertex(location);

            rema.geometry.vertices.push(vertex);
        });


        rema.texture.needsUpdate = true;

        rema.material = new THREE.ParticleBasicMaterial({
            size: 20,
            map: rema.texture,
            opacity: 0.3,
            blending: THREE.AdditiveBlending,
            depthTest: false,
            transparent: true
        });

        rema.particles = new THREE.ParticleSystem(rema.geometry, rema.material);
        rema.layer.add(rema.particles);

        rema.gui = new dat.GUI();

        rema.gui.add(rema.material, 'size', 2, 100).onChange(rema.layer.render);
        rema.gui.add(rema.material, 'opacity', 0.1, 1).onChange(rema.layer.render);

    };
    rema.layer = new rema.layer({map: rema.map}, rema.drawProperties);
};
rema.drawProperties = function() {
    $.getJSON(rema.propertiesPath, function(data) {
        rema.properties = data;
        rema.draw();
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

rema.tube = function() {
    return this;
};
rema.tube.prototype = {
    constructor: rema.tube,
    targetRotation: 0,
    targetRotationOnMouseDown: 0,
    mouseX: 0,
    mouseXOnMouseDown: 0,
    windowHalfX: window.innerWidth / 2,
    windowHalfY: window.innerHeight / 2,
    container: null,
    stats: null,
    camera: null,
    scene: null,
    renderer: null,
    cube: null,
    plane: null,
    init: function() {

        this.targetRotation = 0;
        this.targetRotationOnMouseDown = 0;

        this.mouseX = 0;
        this.mouseXOnMouseDown = 0;

        this.windowHalfX = window.innerWidth / 2;
        this.windowHalfY = window.innerHeight / 2;

        this.container = document.createElement('div');
        document.body.appendChild(this.container);

        var info = document.createElement('div');
        info.style.position = 'absolute';
        info.style.top = '10px';
        info.style.width = '100%';
        info.style.textAlign = 'center';
        info.innerHTML = 'Drag to spin the cube';
        this.container.appendChild(info);

        this.camera = new THREE.PerspectiveCamera(70, window.innerWidth / window.innerHeight, 1, 1000);
        this.camera.position.y = 150;
        this.camera.position.z = 500;

        this.scene = new THREE.Scene();

        // Cube

        var geometry = new THREE.CubeGeometry(200, 200, 200);

        for (var i = 0; i < geometry.faces.length; i += 2) {

            var hex = Math.random() * 0xffffff;
            geometry.faces[ i ].color.setHex(hex);
            geometry.faces[ i + 1 ].color.setHex(hex);

        }

        var material = new THREE.MeshBasicMaterial({vertexColors: THREE.FaceColors, overdraw: 0.5});

        this.cube = new THREE.Mesh(geometry, material);
        this.cube.position.y = 150;
        this.scene.add(this.cube);

        // Plane

        var geometry = new THREE.PlaneGeometry(200, 200);
        geometry.applyMatrix(new THREE.Matrix4().makeRotationX(-Math.PI / 2));

        var material = new THREE.MeshBasicMaterial({color: 0xe0e0e0, overdraw: 0.5});

        this.plane = new THREE.Mesh(geometry, material);
        this.scene.add(this.plane);

        this.renderer = new THREE.CanvasRenderer();
        this.renderer.setSize(window.innerWidth, window.innerHeight);

        this.container.appendChild(this.renderer.domElement);

        this.stats = new Stats();
        this.stats.domElement.style.position = 'absolute';
        this.stats.domElement.style.top = '0px';
        this.container.appendChild(this.stats.domElement);

        document.addEventListener('mousedown', this.onDocumentMouseDown, false);
        document.addEventListener('touchstart', this.onDocumentTouchStart, false);
        document.addEventListener('touchmove', this.onDocumentTouchMove, false);

        //

        window.addEventListener('resize', this.onWindowResize, false);

    },
    onWindowResize: function() {

        this.windowHalfX = window.innerWidth / 2;
        this.windowHalfY = window.innerHeight / 2;

        this.camera.aspect = window.innerWidth / window.innerHeight;
        this.camera.updateProjectionMatrix();

        this.renderer.setSize(window.innerWidth, window.innerHeight);

    },
    onDocumentMouseDown: function(event) {

        event.preventDefault();

        document.addEventListener('mousemove', this.onDocumentMouseMove, false);
        document.addEventListener('mouseup', this.onDocumentMouseUp, false);
        document.addEventListener('mouseout', this.onDocumentMouseOut, false);

        this.mouseXOnMouseDown = event.clientX - this.windowHalfX;
        this.targetRotationOnMouseDown = this.targetRotation;

    },
    onDocumentMouseMove: function(event) {

        this.mouseX = event.clientX - this.windowHalfX;

        this.targetRotation = this.targetRotationOnMouseDown + (this.mouseX - this.mouseXOnMouseDown) * 0.02;

    },
    onDocumentMouseUp: function(event) {

        document.removeEventListener('mousemove', this.onDocumentMouseMove, false);
        document.removeEventListener('mouseup', this.onDocumentMouseUp, false);
        document.removeEventListener('mouseout', this.onDocumentMouseOut, false);

    },
    onDocumentMouseOut: function(event) {

        document.removeEventListener('mousemove', this.onDocumentMouseMove, false);
        document.removeEventListener('mouseup', this.onDocumentMouseUp, false);
        document.removeEventListener('mouseout', this.onDocumentMouseOut, false);

    },
    onDocumentTouchStart: function(event) {

        if (event.touches.length === 1) {

            event.preventDefault();

            this.mouseXOnMouseDown = event.touches[ 0 ].pageX - this.windowHalfX;
            this.targetRotationOnMouseDown = this.targetRotation;

        }

    },
    onDocumentTouchMove: function(event) {

        if (event.touches.length === 1) {

            event.preventDefault();

            this.mouseX = event.touches[ 0 ].pageX - this.windowHalfX;
            this.targetRotation = this.targetRotationOnMouseDown + (this.mouseX - this.mouseXOnMouseDown) * 0.05;

        }

    },
    //

    animate: function() {

        requestAnimationFrame(this.animate);

        this.render();
        this.stats.update();

    },
    render: function() {

        this.plane.rotation.y = this.cube.rotation.y += (this.targetRotation - this.cube.rotation.y) * 0.05;
        this.renderer.render(this.scene, this.camera);

    }
};




//var tube = new rema.tube();
//tube.init();
//tube.animate();
