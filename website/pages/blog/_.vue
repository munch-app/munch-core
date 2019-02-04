<template>
  <div class="container-1200 mtb-32">
    <h1>HUAT Lucky CNY Food Are You?</h1>
    <div class="mtb-16">
      <p>Find out your Lucky Food Fortune Today!</p>
    </div>

    <script>(function (d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s);
      js.id = id;
      js.src = 'https://embed.playbuzz.com/sdk.js';
      fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'playbuzz-sdk'));</script>
    <div class="mt-24 playbuzz" data-id="4110624b-39fa-4794-900e-cd8f95cb7c0d" data-show-info="false"></div>
  </div>
</template>

<script>
  import base64 from 'uuid-base64'

  export default {
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          title: `HUAT Lucky CNY Food Are You? · Munch Singapore`,
          description: 'Find out your Lucky Food Fortune Today!',
          url: `https://www.munch.app/blog/SvQk9G7XIia83vXAeHGyqk`,
          image: 'https://s3-ap-southeast-1.amazonaws.com/munch-static/huat-lucky-cny-food-are-you.jpg'
        },
        breadcrumbs: [
          {
            name: 'Blog',
            item: 'https://www.munch.app/blog'
          },
          {
            name: 'HUAT Lucky CNY Food Are You?',
            item: `https://www.munch.app/blog/SvQk9G7XIia83vXAeHGyqk`
          },
        ]
      })
    }
  }
</script>
