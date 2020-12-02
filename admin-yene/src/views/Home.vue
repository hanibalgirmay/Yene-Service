<template>
  <div class="home">
    <HelloWorld msg="Welcome to Your Vue.js App" />
    <div class="md-layout md-gutter">
      <div class="md-layout-item">
        <DrawGraph />
      </div>
      <div class="md-layout-item"><DonutTypeGraph /></div>
    </div>

    <Table />
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";
import Table from "@/components/Table.vue";
import DrawGraph from "@/components/DrawGraph.vue";
import DonutTypeGraph from "@/components/DonutUserTypeGraph.vue";

export default {
  name: "Home",
  components: {
    HelloWorld,
    Table,
    DrawGraph,
    DonutTypeGraph,
  },
};
</script>
