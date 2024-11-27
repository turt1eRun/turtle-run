import React, { useState } from "react";

type InquiryFormProps = {
  onSubmit: (data: { title: string; content: string }) => void;
};

const InquiryForm: React.FC<InquiryFormProps> = ({ onSubmit }) => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ title, content });
    setTitle("");
    setContent("");
  };

  return (
      <form onSubmit={handleSubmit} className="max-w-4xl mx-auto p-4 bg-white border rounded-lg shadow-md">
        <div className="mb-4">
          <label className="block text-sm font-bold mb-2">제목</label>
          <input
              type="text"
              className="w-full p-2 border border-gray-300 rounded-md"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div className="mb-4">
          <label className="block text-sm font-bold mb-2">내용</label>
          <textarea
              className="w-full p-2 border border-gray-300 rounded-md"
              rows={5}
              value={content}
              onChange={(e) => setContent(e.target.value)}
          />
        </div>
        <button
            type="submit"
            className="px-4 py-2 text-white bg-green-500 rounded-md hover:bg-green-600"
        >
          제출하기
        </button>
      </form>
  );
};

export default InquiryForm;
