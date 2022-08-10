<template>
  <div id="map">
    <Map/>
  </div>
</template>

<script>
  import Map from '@/components/map.vue'

  export default {
    name: 'goodsMap',
    components: {
      Map,
    }
  }
</script>

<style>
  #map {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin: 0;
    padding: 0;
  }
</style>
