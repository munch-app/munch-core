<template>
  <nav class="zero hr-top index-6 bg-white">
    <div class="NavFooter flex container">
      <div class="Brand flex">
        <img style="height: 40px" src="~/assets/img/MunchLogoTitled.svg">
      </div>
      <div class="MunchApp">
        <h4 class="s500">Munch App</h4>
        <a href="https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754" target="_blank">Munch iOS App</a>
        <a href="https://play.google.com/store/apps/details?id=app.munch.munchapp" target="_blank">Munch Android</a>
      </div>

      <div class="Company">
        <h4 class="s500">Company</h4>
        <nuxt-link to="/support">Support</nuxt-link>
        <nuxt-link to="/support/privacy">Privacy Policy</nuxt-link>
        <nuxt-link to="/support/terms">Terms of Use</nuxt-link>
      </div>

      <div class="Product">
        <h4 class="s500">Product</h4>
        <nuxt-link to="/about">About</nuxt-link>
        <nuxt-link to="/press">Press</nuxt-link>
        <a href="https://partner.munch.app" target="_blank">Content Partners</a>
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
    font-size: 15px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.75);

    padding: 4px 0;
    display: block;

    &:hover {
      cursor: pointer;
    }

    &:first-of-type {
      margin-top: 8px;
    }
  }

  @media (max-width: 575.98px) {
    .NavFooter {
      flex-direction: column;
    }
  }
</style>
