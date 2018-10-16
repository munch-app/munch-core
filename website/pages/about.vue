<template>
  <div class="About">
    <section class="Munch">
      <div class="Brand container-1200 content">
        <div class="content-image">
          <img src="/img/about/Munch Logo@3x.png">
        </div>
        <div>
          <div v-html="section1.heading"/>
          <div v-html="section1.paragraph"/>
        </div>
      </div>
    </section>

    <section class="Vision secondary-050-bg">
      <div class="content container-1200">
        <div class="content-image">
        </div>
        <div class="content-text">
          <div v-html="section2.heading"/>
          <div v-html="section2.paragraph"/>
        </div>
      </div>
    </section>

    <section class="Mission">
      <div class="content container-1200">
        <div class="content-image"/>
        <div class="content-text">
          <div v-html="section3.heading"/>
          <div v-html="section3.paragraph"/>
        </div>
      </div>
    </section>

    <section class="Contact primary-050-bg">
      <div class="content container-1200">
        <div class="content-image"/>
        <div class="content-text">
          <div v-html="section4.heading"/>
          <div v-html="section4.paragraph"/>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  export default {
    head() {
      return {title: 'About · Munch'}
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
  section {
    padding-top: 64px;
    padding-bottom: 64px;
  }

  div.content {
    display: flex;
    flex: 1;

    img {
      width: 70%;
      padding-left: 64px;
      padding-right: 64px;
    }

    .content-image {
      display: flex;
      align-items: center;
      justify-content: flex-end;
    }

    .content-text {
      flex: 0 0 50%;
    }

    @media (max-width: 767.98px) {
      flex-direction: column;

      img {
        width: 100%;
      }
    }
  }

  .Mission div.content {
    flex-direction: row-reverse;

    @media (max-width: 767.98px) {
      flex-direction: column;
    }

    .content-image {
      justify-content: flex-start;
    }
  }

  .Munch {
    background-size: cover;
    /*background: url("/img/about/background.jpg") 100%;*/

    .Brand {
      display: flex;

      img {
        padding-left: 32px;
        padding-right: 32px;
      }
    }
  }
</style>
