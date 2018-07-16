<template>
  <b-navbar toggleable="md" variant="light">
    <div class="container">

      <b-navbar-brand>
        <img class="MunchLogo" src="/img/logo-placeholder.png"  href="/">
        <input type="text" class="search-bar" placeholder="Search e.g. Italian in Marina Bay">
        <img class="face-160-x-160" src="/img/profile-placeholder.png">
      </b-navbar-brand>

      <b-collapse is-nav id="nav_collapse">

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">


        </b-navbar-nav>
      </b-collapse>
    </div>
  </b-navbar>
</template>

<style>

  .container{
    color: rgba(0,0,0,0.6);
  }

  .MunchLogo {
    position: absolute;
    left: 24px;
    top: 17px;
    height: 32px;
    width: 32px;
  }

  .search-bar {
    position: absolute;
    left: 80px;
    height: 32px;
    width: 360px;
    background-color: #FFFFFF;
    box-shadow: inset 0 1px 1px 0 rgba(0,0,0,0.5), 0 1px 2px 0 rgba(0,0,0,0.5);
    color: rgba(0,0,0,0.6);
    font-family: "Open Sans";
    font-size: 13px;
    line-height: 18px;
  }

  .face-160-x-160 {
    position: absolute;
    right: 24px;
    top:16px;
    height: 32px;
    width: 32px;
  }
</style>
