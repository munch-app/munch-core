<template>
  <div class="HeaderMenu" v-if="$store.state.layout.menu" v-on-clickaway="onClickAway">
    <div class="Triangle"/>

    <div class="NavLink Elevation1">
      <div @click="onMenuClick('/login')">Login</div>
      <div @click="onMenuClick('/')">Home</div>
      <div @click="onMenuClick('/profile')">View Profile</div>
      <hr>
      <div @click="onMenuClick('/about')">About Munch</div>
      <div @click="onMenuClick('/contact')">Contact Us</div>
      <hr>
      <div><a href="https://partner.munch.app" target="_blank">Content Partners</a></div>
      <hr>
      <div @click="onMenuClick('/support')">Support</div>
      <hr>
      <div @click="onMenuClick('/logout')">Logout</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "HeaderMenu",
    methods: {
      onMenuClick(to) {
        this.$store.commit('layout/showMenu', false)

        this.$router.push({'path': to})
      },
      onClickAway() {
        if (this.$store.state.layout.menu) {
          this.$store.commit('layout/showMenu', false)
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .HeaderMenu {
    z-index: 1000;

    @media (min-width: 768px) {
      max-width: 720px;
      padding-right: 15px;
      padding-left: 15px;
      margin-right: auto;
      margin-left: auto;

      display: flex;
      justify-content: flex-end;
    }

    @media (min-width: 992px) {
      max-width: 960px;

    }

    @media (min-width: 1200px) {
      max-width: 1140px;
    }
  }

  .NavLink {
    z-index: 1000;
    position: absolute;
    background: white;
    padding: 8px 0;

    hr {
      margin: 8px 0;
    }

    & > div {
      padding: 8px 16px;

      &:hover {
        cursor: pointer;
      }
    }

    width: 100%;

    @media (min-width: 768px) {
      width: 200px;
      border-radius: 3px;
      margin-top: -1px;
    }
  }

  .Triangle {
    position: absolute;
    width: 0;
    height: 0;
    border-left: 12px solid transparent;
    border-right: 12px solid transparent;
    border-bottom: 16px solid white;

    display: none;

    @media (min-width: 768px) {
      z-index: 1500;
      display: block;
      margin-top: -10px;
      margin-right: 4px;
    }
  }
</style>
