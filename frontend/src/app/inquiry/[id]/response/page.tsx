'use client';

import React, { useState } from "react";
import { useRouter } from "next/router";

export default function ResponsePage() {
  const router = useRouter();
  const { id } = router.query;

  const [response, setResponse] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!id) {
      alert("올바른 문의 ID를 찾을 수 없습니다.");
      return;
    }

    if (!response) {
      alert("답글 내용을 작성해주세요.");
      return;
    }

    try {
      const res = await fetch(`/api/inquiries/${id}/response`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ response }),
      });

      if (res.ok) {
        alert("답글이 성공적으로 등록되었습니다!");
        router.push(`/inquiry/${id}`); // 올바른 템플릿 리터럴 사용
      } else {
        throw new Error("답글 등록 실패");
      }
    } catch (error: any) {
      alert(error.message || "알 수 없는 오류가 발생했습니다.");
    }
  };

  return (
      <div className="max-w-4xl mx-auto bg-white p-6 border rounded-lg shadow-md">
        <h2 className="text-lg font-bold mb-4">답글 작성</h2>

        <form onSubmit={handleSubmit}>
        <textarea
            className="w-full h-40 p-3 border border-gray-300 rounded-lg mb-4"
            placeholder="답글 내용을 입력해주세요."
            value={response}
            onChange={(e) => setResponse(e.target.value)}
        />
          <button
              type="submit"
              className="w-full bg-green-500 text-white p-3 rounded-lg hover:bg-green-600"
          >
            답글 작성
          </button>
        </form>
      </div>
  );
}
