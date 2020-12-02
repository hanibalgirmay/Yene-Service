<template>
    <div>
       <h1>Service Providers</h1> 
       <md-button class="md-primary"><router-link to="provider/add">Add Provider</router-link></md-button>
       <ProviderTable />
    </div>
</template>
<script>
import ProviderTable from '../components/providers/providersTable';

export default {
    name:"",
    components:{
        ProviderTable
    }
}
</script>