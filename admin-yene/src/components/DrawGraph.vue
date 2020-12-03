<template>
  <div>
     <graph-bar
            :width="600"
            :height="400"
            :axis-min="0"
            :axis-max="50"
            :labels="[ '1Q', '2Q', '3Q', '4Q' ]"
            :values="values">
        <note :text="'Bar Chart'"></note>
        <tooltip :names="names" :position="'left'"></tooltip>
        <legends :names="names" :filter="true"></legends>
    </graph-bar>

  </div>
</template>
<script>
import {db} from '../firebase.config';

  export default {
    name: 'DrawGraph',
    data:() => ({
     names: [ "MS", "Apple", "Google" ],
        values: [
            [ 10, 5, 5, 5 ],
            [ 40, 10, 10, 10 ],
            [ 30, 30, 30, 30 ]
        ] 
    }),
    methods: {
      getValueGraph(){
        db.collection("Users")
          .get()
          .then(()=>{

          })
          .catch((er) => console.log(er))
      }
    },
    created() {
      this.getValueGraph();
    },
   
  }
</script>
<style lang="css" scoped>
  .page-container {
    min-height: 300px;
    overflow: hidden;
    position: relative;
    border: 1px solid rgba(#000, .12);
  }