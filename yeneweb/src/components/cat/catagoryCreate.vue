<template>
<div class="mt-3">
    <div class="container">
        <div class="row justify-content-center">
        <div class="col-md-5">
            <h3 class="text-center font-weight-bold text-info">Add Catagory</h3>
            <form @submit.prevent="onFormSubmit">
                <div class="form-group">
                    <label>Catagory Name</label>
                    <input type="text" class="form-control" v-model="catagory.name" required>
                </div>

                <div class="form-group">
                    <label>Catagory Discription</label>
                    <textarea rows="5" type="text" class="form-control" v-model="catagory.disc" required>
                    </textarea>
                </div>

                <div class="form-group">
                    <label>Coolse File </label>
                    <input type="file" ref="file" class="form-control" >
                    
                </div>

                <div class="form-group">
                    <button class="btn btn-primary btn-block">IPdate catagory</button>
                </div>

            </form>
            <form>
            </form>
        </div>
    </div>
    </div>
</div>
</template>

<script>
import db from "../../db.js";


    export default {
        data() {
            return {
                catagory: {
                },
            }
        },

        methods: {
            onFormSubmit(event) {  
                event.preventDefault()
                db.collection('catagories').add(this.catagory).then(() => {
                    alert("Catagory successfully created!");
                    // const timestamp = Math.round(+new Date()/1000); unix time stamp
                    var today = new Date();
                    var date = today.getFullYear()+'/'+(today.getMonth()+1)+'/'+today.getDate();
                    var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
                    var timestamp = date+' '+time;
                    this.catagory.name = ''
                    this.catagory.disc = ''
                    this.catagory.timestamp = timestamp
                
                }).catch((error) => {
                    console.log(error);
                });
            }
        }
    }
</script>