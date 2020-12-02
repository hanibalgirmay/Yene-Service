<template>
  <div class="about">
    <h1>Registered Users</h1>
    <AddUser />
    <UsersTable />
    
  </div>
</template>

<script>
import UsersTable from "../components/users/usersTable";
import AddUser from '../components/users/addNewUser';

export default {
  components: {
    UsersTable,
    AddUser
  },
  
};
</script>

<style></style>
