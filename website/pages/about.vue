<template>
  <div class="About">
    <section class="Munch container">
      <div v-html="section1.heading"/>
      <div v-html="section1.paragraph"/>
    </section>

    <section class="Vision container">
      <div v-html="section2.heading"/>
      <div v-html="section2.paragraph"/>
    </section>

    <section class="Mission container">
      <div v-html="section3.heading"/>
      <div v-html="section3.paragraph"/>
    </section>

    <section class="Contact container">
      <div v-html="section4.heading"/>
      <div v-html="section4.paragraph"/>
    </section>
  </div>
</template>

<script>
  export default {
    head() {
      return {title: 'About | Munch'}
    },
    asyncData({Prismic}) {
      return Prismic.getSingle('about')
        .then(({data}) => {
          return {
            section1: {
              heading: Prismic.asHtml(data.heading_1),
              paragraph: Prismic.asHtml(data.paragraph_1),
              image: data.image_1.url
            },
            section2: {
              heading: Prismic.asHtml(data.heading_2),
              paragraph: Prismic.asHtml(data.paragraph_2),
              image: data.image_2.url
            },
            section3: {
              heading: Prismic.asHtml(data.heading_3),
              paragraph: Prismic.asHtml(data.paragraph_3),
              image: data.image_3.url
            },
            section4: {
              heading: Prismic.asHtml(data.heading_4),
              paragraph: Prismic.asHtml(data.paragraph_4),
              image: data.image_4.url
            }
          }
        })
    },
  }
</script>

<style scoped lang="less">
  .About {
    padding-bottom: 64px;
  }

  section {
    margin-top: 24px;
    margin-bottom: 48px;
  }
</style>
