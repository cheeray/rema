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

(function(window, undefined) {

    function GmapLayer(options, callback) {
        this.bindAll();
        this.callback = callback;
        this.initialize(options || {});

        this.firstRun = true;

        if (options.map) {
            this.setMap(options.map);
        }
    }

    /**
     * Extend OverlayView.
     * @see https://developers.google.com/maps/documentation/javascript/reference#OverlayView
     * @type {google.maps.OverlayView}
     */
    GmapLayer.prototype = new google.maps.OverlayView();

    /**
     * Get browser specifiv CSS transform property.
     * 
     * @return {String} The property.
     */
    GmapLayer.CSS_TRANSFORM = (function() {
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

    /**
     * Bind all methods to the instance.
     */
    GmapLayer.prototype.bindAll = function() {
        var instance = this;

        function bind(name) {
            var method = instance[name];
            if (typeof method != "function") {
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

    /**
     * Initialize the layer with the given options.
     * @param  {Object} options - Options
     */
    GmapLayer.prototype.initialize = function(options) {

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

    /**
     * This method is called once after setMap() is called with a valid map. 
     * @see https://developers.google.com/maps/documentation/javascript/reference#OverlayView
     */
    GmapLayer.prototype.onAdd = function() {

        this.map = this.getMap();

        this.getPanes().overlayLayer.appendChild(this.canvas);

        this.changeHandler = google.maps.event.addListener(
                this.map,
                'bounds_changed',
                this.draw
                );

        this.draw();
    };

    /**
     * This method is called once following a call to setMap(null).
     * @see https://developers.google.com/maps/documentation/javascript/reference#OverlayView
     */
    GmapLayer.prototype.onRemove = function() {

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

    /**
     * This method is called when the layer postion needs an update.
     */
    GmapLayer.prototype.draw = function() {

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

        this.canvas.style[GmapLayer.CSS_TRANSFORM] = 'translate(' +
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

    /**
     * Call this method when the layer's size changed.
     */
    GmapLayer.prototype.resize = function() {

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

    /**
     * This method is called when the Three.js camera needs an update.
     */
    GmapLayer.prototype.update = function() {

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

    /**
     * Renders the layer deferred.
     */
    GmapLayer.prototype.render = function() {
        cancelAnimationFrame(this.animationFrame);
        this.animationFrame = requestAnimationFrame(this.deferredRender);
    };

    /**
     * The final rendering. If you have passed a function to `options.render`
     * it will be executed here.
     */
    GmapLayer.prototype.deferredRender = function() {
        if (typeof this.options.render === false) {
            return;
        } else if (typeof this.options.render === "function") {
            this.options.render();
        } else {
            this.renderer.render(this.scene, this.camera);
        }
    };

    /**
     * Shortcut method to add new geometry to the scene.
     * @param  {Geometry} geometry The Three.js geometry to add.
     */
    GmapLayer.prototype.add = function(geometry) {
        this.scene.add(geometry);
    };

    /**
     * Helper method to convert for LatLng to vertex.
     * @param  {google.maps.LatLng} latLng - The LatLng to convert.
     * @return {THREE.Vector3} The resulting vertex.
     */
    GmapLayer.prototype.fromLatLngToVertex = function(latLng) {
        var projection = this.map.getProjection(),
                point = projection.fromLatLngToPoint(latLng),
                vertex = new THREE.Vector3();

        vertex.x = point.x;
        vertex.y = point.y;
        vertex.z = 0;

        return vertex;
    };
  
})(window);


var styles = [
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
],
        mapOptions = {
            zoom: 4,
            mapTypeControl: false,
            center: new google.maps.LatLng(-33.8678500, 151.2073200),
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            styles: styles
        };
