<template>
  <nav class="zero hr-top index-6 bg-white">
    <div class="NavFooter flex container">
      <div class="Brand">
        <a href="/" class="m-0">
          <img style="height: 30px" src="~/assets/img/LOGO_NAME_TRIM.svg">
        </a>
      </div>
      <div>
        <h4>Mobile App</h4>
        <a href="https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754" target="_blank">iOS</a>
        <a href="https://play.google.com/store/apps/details?id=app.munch.munchapp" target="_blank">Android</a>
      </div>

      <div>
        <h4>Company</h4>
        <nuxt-link to="/support" no-prefetch>Support</nuxt-link>
        <nuxt-link to="/support/privacy" no-prefetch>Privacy Policy</nuxt-link>
        <nuxt-link to="/support/terms" no-prefetch>Terms of Use</nuxt-link>
      </div>

      <div>
        <h4>Product</h4>
        <nuxt-link to="/press" no-prefetch>Press</nuxt-link>
        <nuxt-link to="/partner" no-prefetch>Partner</nuxt-link>
      </div>
    </div>
  </nav>
</template>

<script>
  export default {
    name: "NavFooter"
  }
</script>

<style scoped lang="less">
  nav {
    padding: 0 0 64px;

    > div > div {
      margin-top: 32px;
      margin-right: 88px;
    }
  }

  a {
    color: rgba(0, 0, 0, 0.75);
    text-decoration: none;

    padding: 4px 0;
    display: block;

    &:first-of-type {
      margin-top: 8px;
    }

    &:hover {
      text-decoration: underline;
    }
  }

  @media (max-width: 768px) {
    .NavFooter {
      flex-direction: column;
    }
  }
</style>
