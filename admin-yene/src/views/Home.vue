<template>
  <div class="home">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
    <Table />
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import Table from '@/components/Table.vue'

export default {
  name: 'Home',
  components: {
    HelloWorld,
    Table
  }
}
</script>
