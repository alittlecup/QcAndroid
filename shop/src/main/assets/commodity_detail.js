// { "framework": "Vue"} 

!function(e){function t(r){if(n[r])return n[r].exports;var o=n[r]={i:r,l:!1,exports:{}};return e[r].call(o.exports,o,o.exports,t),o.l=!0,o.exports}var n={};t.m=e,t.c=n,t.i=function(e){return e},t.d=function(e,n,r){t.o(e,n)||Object.defineProperty(e,n,{configurable:!1,enumerable:!0,get:r})},t.n=function(e){var n=e&&e.__esModule?function(){return e.default}:function(){return e};return t.d(n,"a",n),n},t.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},t.p="",t(t.s=7)}([function(e,t,n){"use strict";function r(){return"Web"!==weex.config.env.platform}Object.defineProperty(t,"__esModule",{value:!0}),t.default={methods:{push:function(e){if(r()){var t=weex.config.bundleUrl.split("/").slice(0,-1).join("/")+"/"+e+".js";weex.requireModule("navigator").push({url:t,animated:"true"})}else this.$router.push(e)},pop:function(){r()?weex.requireModule("navigator").pop({animated:"true"}):window.history.back()}}}},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(0),o=function(e){return e&&e.__esModule?e:{default:e}}(r);t.default={data:function(){return{}},methods:{back:function(e){this.pop()}},mixins:[o.default]}},,,function(e,t){e.exports={}},,function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",[n("text",{on:{click:function(t){e.back("commodity_list")}}},[e._v("返回列表页")])])},staticRenderFns:[]},e.exports.render._withStripped=!0},function(e,t,n){var r,o,u=[];u.push(n(4)),r=n(1);var i=n(6);o=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(Object.keys(r).some(function(e){return"default"!==e&&"__esModule"!==e})&&console.error("named exports are not supported in *.vue files."),o=r=r.default),"function"==typeof o&&(o=o.options),o.__file="/Users/yxp/Projects/qc-comodity-weex/src/pages/commodity_detail.vue",o.render=i.render,o.staticRenderFns=i.staticRenderFns,o._scopeId="data-v-c6eebee4",o.style=o.style||{},u.forEach(function(e){for(var t in e)o.style[t]=e[t]}),"function"==typeof __register_static_styles__&&__register_static_styles__(o._scopeId,u),e.exports=r,e.exports.el="true",new Vue(e.exports)}]);