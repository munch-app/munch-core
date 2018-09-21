<template>
  <nav class="zero-spacing nav-bg">
    <div class="NavFooter flex container">
      <div class="Brand">
        <img class="Image" src="/img/MunchLogo.svg">
        <h4 class="primary">Munch</h4>
      </div>
      <div class="Company">
        <h4 class="secondary">Company</h4>
        <nuxt-link to="/press">Press</nuxt-link>
        <nuxt-link to="/about">About</nuxt-link>
        <nuxt-link to="/support">Support</nuxt-link>
      </div>

      <div class="Product">
        <h4 class="secondary">Product</h4>
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
      margin-right: 80px;
    }
  }

  div.Brand, div.Company, div.Product {
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
  }

  div.Brand {
    display: flex;
    align-items: flex-start;
    h4 {
      margin-left: 16px;
    }
  }

  @media (max-width: 575.98px) {
    .NavFooter {
      flex-direction: column;
    }
  }
</style>
