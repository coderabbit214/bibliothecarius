import React from 'react';


const MonacoEditor = dynamic(import("react-monaco-editor"), {ssr: false});
import {MonacoEditorProps} from 'react-monaco-editor';

import dynamic from "next/dynamic";
import styles from '@/styles/jsonEditor.module.css';

const JsonEditor: React.FC<MonacoEditorProps> = (props) => {
  return (
    <div className={styles.editorContainer} tabIndex={-1}>
      <MonacoEditor
        {...props}
        editorDidMount={(editor) => {
          // @ts-ignore
          window.MonacoEnvironment.getWorkerUrl = (
            _moduleId: string,
            label: string
          ) => {
            if (label === "json")
              return "_next/static/json.worker.js";
            if (label === "css")
              return "_next/static/css.worker.js";
            if (label === "html")
              return "_next/static/html.worker.js";
            if (
              label === "typescript" ||
              label === "javascript"
            )
              return "_next/static/ts.worker.js";
            return "_next/static/editor.worker.js";
          };
        }}
        language="json"
        theme="vs-light"
        height="200px"
        options={{
          minimap: {enabled: false},
          scrollBeyondLastLine: false,
          lineNumbers: 'off',
          glyphMargin: false,
          folding: false,
          lineDecorationsWidth: 0,
          lineNumbersMinChars: 0
        }}
      />
    </div>
  );
};

export default JsonEditor;
