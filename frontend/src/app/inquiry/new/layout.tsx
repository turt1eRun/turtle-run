'use client';

export default function NewInquiryLayout({
                                           children,
                                         }: {
  children: React.ReactNode;
}) {
  return (
      <div>
        <header className="mb-6">
          <h2 className="text-xl font-bold">새 문의 작성</h2>
        </header>
        {children}
      </div>
  );
}
