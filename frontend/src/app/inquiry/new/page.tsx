'use client';

import React from "react";
import InquiryForm from "@/components/inquiry/InquiryForm";

export default function NewInquiryPage() {
  const handleSubmit = async (data: { title: string; content: string }) => {
    const res = await fetch("/api/inquiries", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (res.ok) {
      alert("문의가 성공적으로 등록되었습니다!");
    }
  };

  return <InquiryForm onSubmit={handleSubmit} />;
}
