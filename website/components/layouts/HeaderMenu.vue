<template>
  <div class="HeaderMenu" v-if="show">
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
      <div @click="onMenuClick('/kb/terms-of-use')">Terms of Use</div>
      <div @click="onMenuClick('/kb/privacy-policy')">Privacy Policy</div>
      <hr>
      <div @click="onMenuClick('/logout')">Logout</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "HeaderMenu",
    props: {
      show: {
        type: Boolean,
        required: true
      }
    },
    methods: {
      onMenuClick(to) {
        this.$emit('onHidden')
        this.$router.push({'path': to})
      }
    }
  }
</script>

<style scoped lang="less">
  .HeaderMenu {
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
    }
  }

  .Triangle {
    width: 0;
    height: 0;
    border-left: 12px solid transparent;
    border-right: 12px solid transparent;
    border-bottom: 16px solid white;

    display: none;

    @media (min-width: 768px) {
      display: block;
      margin-top: -10px;
      margin-right: 4px;
    }
  }
</style>
