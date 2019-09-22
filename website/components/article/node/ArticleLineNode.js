import {Node} from 'tiptap'
import ArticleLineNode from './ArticleLine.vue'

export default class Line extends Node {
  get name() {
    return 'line'
  }

  get schema() {
    return {
      group: 'block',
      draggable: true,
      selectable: true,

      parseDOM: [{tag: 'hr',}],
      toDOM: node => ['hr', node.attrs],
    }
  }

  commands({type}) {
    return (attrs) => (state, dispatch) => {
      const node = type.create(attrs)
      dispatch(state.tr.replaceSelectionWith(node))
    }
  }

  get view() {
    return ArticleLineNode
  }
}
