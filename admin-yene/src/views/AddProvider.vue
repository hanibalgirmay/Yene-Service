<template>
  <div>
    <h1>Map Location</h1>
    {{ coordinates }}
    <GmapMap
      :center="{ lat: 9.014476799999999, lng: 38.797312 }"
      :zoom="7"
      style="width:670px;height:300px"
    >
    </GmapMap>
  </div>
</template>

<script>
export default {
  name: "AddProvider",
  data: () => ({
    coordinates: {
      lat: 0,
      lng: 0,
    },
  }),
  created() {
    this.$getLocation({})
      .then((coordinates) => {
        this.coordinates = coordinates;
      })
      .catch((err) => {
        console.log(err);
        alert(err);
      });
  },
};
</script>

<style></style>
