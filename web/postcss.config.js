module.exports = {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
}

const purgecss = [
  "@fullhuman/postcss-purgecss",
  {
    // https://purgecss.com/configuration.html#options
    content: ["./components/**/*.tsx", "./pages/**/*.tsx"],
    css: [],
    whitelistPatternsChildren: [/monaco-editor/], // so it handles .monaco-editor .foo .bar
    defaultExtractor: content => content.match(/[\w-/.:]+(?<!:)/g) || []
  }
];
